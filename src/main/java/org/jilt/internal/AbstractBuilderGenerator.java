package org.jilt.internal;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;
import org.jilt.Builder;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.type.DeclaredType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractBuilderGenerator implements BuilderGenerator {
    private final Elements elements;
    private final Filer filer;

    private final TypeElement targetClassType;
    private final List<? extends VariableElement> attributes;
    private final Builder builderAnnotation;
    private final ExecutableElement targetCreationMethod;

    private final String builderClassPackage;
    private final ClassName builderClassClassName;

    AbstractBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, ExecutableElement targetCreationMethod,
            Elements elements, Filer filer) {
        this.elements = elements;
        this.filer = filer;

        this.targetClassType = targetClass;
        this.attributes = attributes;
        this.builderAnnotation = builderAnnotation;
        this.targetCreationMethod = targetCreationMethod;

        this.builderClassPackage = this.initBuilderClassPackage();
        this.builderClassClassName = ClassName.get(this.builderClassPackage(),
                this.builderClassStringName());
    }

    @Override
    public final void generateBuilderClass() throws Exception {
        this.generateClassesNeededByBuilder();

        // builder class
        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(this.builderClassClassName)
                .addAnnotation(this.generatedAnnotation())
                .addModifiers(this.determineBuilderClassModifiers())
                .addTypeVariables(this.builderClassTypeParameters());

        // add a static factory method to the builder class
        MethodSpec staticFactoryMethod = this.makeStaticFactoryMethod();
        if (staticFactoryMethod != null) {
            builderClassBuilder.addMethod(staticFactoryMethod);
        }

        // add a static toBuilder() method to the builder class
        MethodSpec toBuilderMethod = this.makeToBuilderMethod();
        if (toBuilderMethod != null) {
            builderClassBuilder.addMethod(toBuilderMethod);
        }

        // add a field and setter for each attribute of the built class
        for (VariableElement attribute : attributes) {
            String fieldName = attributeSimpleName(attribute);
            TypeName fieldType = TypeName.get(attribute.asType());

            builderClassBuilder.addField(FieldSpec
                    .builder(fieldType, fieldName,
                            this.builderClassNeedsToBeAbstract()
                                ? Modifier.PROTECTED
                                : Modifier.PRIVATE)
                    .build());

            MethodSpec setterMethod = this.generateBuilderSetterMethod(attribute);
            if (setterMethod != null) {
                builderClassBuilder.addMethod(setterMethod);
            }
        }

        // add the 'build' method
        builderClassBuilder.addMethod(this.makeBuildMethod());

        enhance(builderClassBuilder);

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage(), builderClassBuilder.build())
                .build();
        javaFile.writeTo(filer);
    }

    private Modifier[] determineBuilderClassModifiers() {
        List<Modifier> modifiers = new ArrayList<Modifier>(2);
        if (this.builderClassNeedsToBeAbstract()) {
            // if the creation method is private,
            // we need to make the Builder abstract
            modifiers.add(Modifier.ABSTRACT);
            // if the Builder is in the same package as the target class,
            // make it package-private (it will only be available through the target class anyway,
            // because of the creation method being private)
            if (!this.builderClassPackage.equals(this.determineTargetClassPackage())) {
                modifiers.add(Modifier.PUBLIC);
            }
        } else {
            modifiers.add(Modifier.PUBLIC);
        }
        return modifiers.toArray(new Modifier[]{});
    }

    protected MethodSpec makeStaticFactoryMethod() {
        if (this.builderClassNeedsToBeAbstract()) {
            // if the Builder class has to be abstract,
            // don't generate a static factory method
            // (since you can't instantiate an abstract class)
            return null;
        }

        return MethodSpec
                .methodBuilder(this.builderFactoryMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(this.builderClassTypeParameters())
                .returns(builderFactoryMethodReturnType())
                .addStatement("return new $T()", this.builderClassTypeName())
                .build();
    }

    protected MethodSpec makeToBuilderMethod() {
        // if the @Builder annotation has an empty toBuilder attribute,
        // don't generate this method
        if (this.builderAnnotation.toBuilder().isEmpty()) {
            return null;
        }

        String targetClassParam = Utils.deCapitalize(this.targetClassSimpleName().toString());
        MethodSpec.Builder toBuilderMethod = MethodSpec
                .methodBuilder(this.builderAnnotation.toBuilder())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(this.builderClassTypeParameters())
                .returns(this.builderClassTypeName())
                .addParameter(ParameterSpec
                        .builder(this.targetClassTypeName(), targetClassParam)
                        .build());

        CodeBlock.Builder methodBody = CodeBlock.builder();
        String returnVarName = this.builderClassMethodParamName();
        methodBody.addStatement("$1T $2N = new $1T()", this.builderClassTypeName(),
                returnVarName);
        // iterate through all attributes,
        // and add a setter statement to the method body for each
        for (VariableElement attribute : attributes) {
            String attributeAccess = this.accessAttributeOfTargetClass(attribute);
            methodBody.addStatement("$L.$L($L.$L)",
                    returnVarName,
                    this.setterMethodName(attribute),
                    targetClassParam, attributeAccess);
        }
        methodBody.addStatement("return $L", returnVarName);

        return toBuilderMethod
                .addCode(methodBody.build())
                .build();
    }

    private MethodSpec makeBuildMethod() {
        MethodSpec.Builder buildMethod = MethodSpec
                .methodBuilder(this.buildMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(this.targetClassTypeName());
        if (this.builderClassNeedsToBeAbstract()) {
            buildMethod.addModifiers(Modifier.ABSTRACT);
        } else {
            String attributes = Utils.join(this.attributeNames());
            if (this.targetCreationMethodIsConstructor()) {
                buildMethod.addStatement("return new $T($L)", this.targetClassTypeName(), attributes);
            } else {
                buildMethod.addStatement("return $T.$L($L)",
                        // using ClassName gets rid of any type parameters the class might have
                        ClassName.get((TypeElement) this.targetCreationMethod.getEnclosingElement()),
                        this.targetCreationMethod.getSimpleName(),
                        attributes);
            }
        }
        return buildMethod.build();
    }

    protected final String accessAttributeOfTargetClass(VariableElement attribute) {
        String fieldName = this.attributeSimpleName(attribute);
        for (Element member : this.elements.getAllMembers(this.targetClassType)) {
            // if there's a getter method, use it
            if (elementIsMethodWithoutArgumentsCalled(member, "get" + Utils.capitalize(fieldName))) {
                return member.getSimpleName().toString() + "()";
            }
            // getters for boolean properties start with "is" instead of "get"
            if (elementIsMethodWithoutArgumentsCalled(member, "is" + Utils.capitalize(fieldName))) {
                return member.getSimpleName().toString() + "()";
            }
            // if there's a no-argument method with the field name,
            // like with Records, use that
            if (elementIsMethodWithoutArgumentsCalled(member, fieldName)) {
                return member.getSimpleName().toString() + "()";
            }
        }
        // if we haven't found a sensible method, fall back to the field name
        return fieldName;
    }

    private static boolean elementIsMethodWithoutArgumentsCalled(Element element, String methodName) {
        return element.getKind() == ElementKind.METHOD &&
                element.getSimpleName().toString().equals(methodName) &&
                ((ExecutableElement) element).getParameters().isEmpty();
    }

    private List<String> attributeNames() {
        List<String> ret = new ArrayList<String>(attributes.size());
        for (VariableElement attribute : attributes) {
            ret.add(attributeSimpleName(attribute));
        }
        return ret;
    }

    protected abstract void generateClassesNeededByBuilder() throws Exception;

    protected abstract TypeName builderFactoryMethodReturnType();

    protected abstract void enhance(TypeSpec.Builder builderClassBuilder);

    private MethodSpec generateBuilderSetterMethod(VariableElement attribute) {
        return this.generateSetterMethod(attribute, /* mangleTypeParameters */ false,
                /* abstractMethod */ false);
    }

    protected MethodSpec generateSetterMethod(VariableElement attribute,
            boolean mangleTypeParameters, boolean abstractMethod) {
        String fieldName = this.attributeSimpleName(attribute);
        TypeName parameterType = this.attributeType(attribute, mangleTypeParameters);
        MethodSpec.Builder setter = MethodSpec
                .methodBuilder(this.setterMethodName(attribute))
                .addModifiers(Modifier.PUBLIC)
                .returns(abstractMethod
                        // for setters in interfaces, we want to return a different interface type
                        ? this.returnTypeForSetterFor(attribute, mangleTypeParameters)
                        // for setters in the Builder class, we want to return itself
                        // (this is useful for toBuilder() usecases,
                        // where we chain methods of the Builder class directly)
                        : this.builderClassTypeName())
                .addParameter(this.setterParameterSpec(attribute, parameterType));
        if (abstractMethod) {
            setter.addModifiers(Modifier.ABSTRACT);
        } else {
            setter.addStatement("this.$1L = $1L", fieldName)
                    .addStatement("return this");
        }

        return setter.build();
    }

    protected abstract TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters);

    protected final TypeName attributeType(VariableElement attribute,
            boolean withMangledTypeParameters) {
        TypeName ret = TypeName.get(attribute.asType());
        return withMangledTypeParameters ? this.mangleTypeName(ret) : ret;
    }

    protected final TypeName mangleTypeName(TypeName ret) {
        if (ret instanceof TypeVariableName) {
            // if this is a type variable, we need to mangle it
            TypeVariableName typeVariableName = (TypeVariableName) ret;
            return this.mangleTypeParameter(typeVariableName);
        }
        // if this is an entire parameterized type, we need to mangle it
        if (ret instanceof ParameterizedTypeName) {
            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) ret;
            return ParameterizedTypeName.get(parameterizedTypeName.rawType,
                    this.mangleTypeParameters(parameterizedTypeName.typeArguments).toArray(new TypeName[]{}));
        }
        return ret;
    }

    private List<TypeName> mangleTypeParameters(List<TypeName> typeParameters) {
        List<TypeName> ret = new ArrayList<TypeName>(typeParameters.size());
        for (TypeName typeParameter : typeParameters) {
            if (typeParameter instanceof TypeVariableName) {
                // if this is a type variable, we need to mangle it
                TypeVariableName typeVariableName = (TypeVariableName) typeParameter;
                ret.add(this.mangleTypeParameter(typeVariableName));
            } else if (typeParameter instanceof WildcardTypeName) {
                WildcardTypeName wildcardTypeName = (WildcardTypeName) typeParameter;
                List<TypeName> lowerBounds = this.mangleTypeParameters(wildcardTypeName.lowerBounds);
                List<TypeName> upperBounds = this.mangleTypeParameters(wildcardTypeName.upperBounds);
                if (!lowerBounds.isEmpty()) {
                    ret.add(WildcardTypeName.supertypeOf(lowerBounds.get(0)));
                } else if (!upperBounds.isEmpty()) {
                    ret.add(WildcardTypeName.subtypeOf(upperBounds.get(0)));
                } else {
                    ret.add(typeParameter);
                }
            } else if (typeParameter instanceof ParameterizedTypeName) {
                // if this is an entire parameterized type, we need to mangle it recursively
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeParameter;
                ret.add(ParameterizedTypeName.get(parameterizedTypeName.rawType,
                    this.mangleTypeParameters(parameterizedTypeName.typeArguments).toArray(new TypeName[]{})));
            } else {
                ret.add(typeParameter);
            }
        }
        return ret;
    }

    protected final TypeVariableName mangleTypeParameter(TypeVariableName typeVariableName) {
        return TypeVariableName.get(typeVariableName.name + "_",
                // copy over the bounds, if there are any, recursively mangling them too
                this.mangleTypeParameters(typeVariableName.bounds).toArray(new TypeName[]{}));
    }

    protected final ParameterSpec setterParameterSpec(VariableElement attribute, TypeName parameterType) {
        ParameterSpec.Builder ret = ParameterSpec
                .builder(parameterType, this.attributeSimpleName(attribute))
                .addModifiers(Modifier.FINAL);
        Set<DeclaredType> presentAnnotations = new HashSet<DeclaredType>();
        this.addAnnotationsToParam(ret, attribute.getAnnotationMirrors(), presentAnnotations);
        this.addAnnotationsToParam(ret, attribute.asType().getAnnotationMirrors(), presentAnnotations);
        return ret.build();
    }

    private void addAnnotationsToParam(ParameterSpec.Builder param, List<? extends AnnotationMirror> annotations, Set<DeclaredType> presentAnnotations) {
        for (AnnotationMirror annotation : annotations) {
            if (!presentAnnotations.contains(annotation.getAnnotationType()) && this.isAnnotationAllowedOnParam(annotation)) {
                param.addAnnotation(AnnotationSpec.get(annotation));
                presentAnnotations.add(annotation.getAnnotationType());
            }
        }
    }

    private boolean isAnnotationAllowedOnParam(AnnotationMirror annotation) {
        Target targetAnnotation = annotation.getAnnotationType().asElement().getAnnotation(Target.class);
        if (targetAnnotation == null) {
            return true;
        }

        for (ElementType elementType : targetAnnotation.value()) {
            if (ElementType.PARAMETER.equals(elementType) || "TYPE_USE".equals(elementType.name())) {
                return true;
            }
        }

        return false;
    }

    private String initBuilderClassPackage() {
        String annotationBuilderPackageName = builderAnnotation.packageName();
        return annotationBuilderPackageName.isEmpty()
                ? this.determineTargetClassPackage()
                : annotationBuilderPackageName;
    }

    protected final String builderClassMethodParamName() {
        return Utils.deCapitalize(this.builderClassStringName());
    }

    protected final String builderClassStringName() {
        String annotationBuilderClassName = builderAnnotation.className();
        return annotationBuilderClassName.isEmpty()
                ? this.targetClassSimpleName() + "Builder"
                : annotationBuilderClassName;
    }

    protected final Filer filer() {
        return filer;
    }

    protected final Name targetClassSimpleName() {
        return this.targetClassType.getSimpleName();
    }

    protected final TypeName targetClassTypeName() {
        return this.targetCreationMethodIsConstructor()
                ? TypeName.get(this.targetClassType.asType())
                : TypeName.get(this.targetCreationMethod.getReturnType());
    }

    protected final List<? extends VariableElement> attributes() {
        return attributes;
    }

    protected final String builderClassPackage() {
        return builderClassPackage;
    }

    protected final TypeName builderClassTypeName() {
        List<TypeVariableName> typeVariableNames = this.builderClassTypeParameters();
        return typeVariableNames.isEmpty()
                ? this.builderClassClassName
                : ParameterizedTypeName.get(this.builderClassClassName,
                    typeVariableNames.toArray(new TypeVariableName[0]));
    }

    protected final List<TypeVariableName> builderClassTypeParameters() {
        List<? extends TypeParameterElement> typeParameterElements = this.targetCreationMethodIsConstructor()
                ? this.targetClassType.getTypeParameters()
                : this.targetCreationMethod.getTypeParameters();
        List<TypeVariableName> ret = new ArrayList<TypeVariableName>(
                typeParameterElements.size());
        for (TypeParameterElement typeParameterEl : typeParameterElements) {
            ret.add(TypeVariableName.get(typeParameterEl));
        }
        return ret;
    }

    private boolean targetCreationMethodIsConstructor() {
        if (this.targetCreationMethod == null) {
            // if we don't have targetCreationMethod set,
            // that means @Builder was placed on the class itself,
            // so we should use the implicit all-argument constructor
            return true;
        }
        // but, we do save the constructor if @Builder was placed on it,
        // so check if the creation method's simple name is "<init>",
        // which is what constructors are called in bytecode
        return "<init>".equals(this.targetCreationMethod.getSimpleName().toString());
    }

    protected final String setterMethodName(VariableElement attribute) {
        String annotationSetterPrefix = builderAnnotation.setterPrefix();
        String attributeSimpleName = attributeSimpleName(attribute);
        return annotationSetterPrefix.isEmpty()
                ? attributeSimpleName
                : annotationSetterPrefix + Utils.capitalize(attributeSimpleName);
    }

    protected final String attributeSimpleName(VariableElement attribute) {
        return attribute.getSimpleName().toString();
    }

    private String determineTargetClassPackage() {
        return this.elements.getPackageOf(this.targetClassType).getQualifiedName().toString();
    }

    protected final String builderFactoryMethodName() {
        String annotationFactoryMethod = this.builderAnnotation.factoryMethod();
        return annotationFactoryMethod.isEmpty()
                ? Utils.deCapitalize(this.targetClassSimpleName().toString())
                : annotationFactoryMethod;
    }

    protected final String buildMethodName() {
        String annotationBuildMethod = builderAnnotation.buildMethod();
        return annotationBuildMethod.isEmpty()
                ? "build"
                : annotationBuildMethod;
    }

    protected final boolean builderClassNeedsToBeAbstract() {
        if (this.targetCreationMethod == null) {
            // we assume the implicit constructor is public
            return false;
        }
        return this.targetCreationMethod.getModifiers().contains(Modifier.PRIVATE);
    }

    protected final AnnotationSpec generatedAnnotation() throws Exception {
        Class<?> generatedAnnotationClass = determineGeneratedAnnotationClass();
        return AnnotationSpec
                .builder(generatedAnnotationClass)
                .addMember("value", "$S", "Jilt-1.6")
                .build();
    }

    private Class<?> determineGeneratedAnnotationClass() throws Exception {
        try {
            // available since 9
            return Class.forName("javax.annotation.processing.Generated");
        } catch (ClassNotFoundException e) {
            return Class.forName("javax.annotation.Generated");
        }
    }
}
