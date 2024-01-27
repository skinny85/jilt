package org.jilt.internal;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
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
import org.jilt.Opt;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractBuilderGenerator implements BuilderGenerator {
    private final Elements elements;
    private final Filer filer;

    private final TypeElement targetClassType;
    private final List<? extends VariableElement> attributes;
    private final Set<VariableElement> optionalAttributes;
    private final Builder builderAnnotation;
    private final ExecutableElement targetFactoryMethod;

    private final String builderClassPackage;
    private final ClassName builderClassClassName;

    AbstractBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, ExecutableElement targetFactoryMethod,
            Elements elements, Filer filer) {
        this.elements = elements;
        this.filer = filer;

        this.targetClassType = targetClass;
        this.attributes = attributes;
        this.optionalAttributes = initOptionalAttributes();
        this.builderAnnotation = builderAnnotation;
        this.targetFactoryMethod = targetFactoryMethod;

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
                .addModifiers(Modifier.PUBLIC)
                .addTypeVariables(this.builderClassTypeParameters());

        // add a static factory method to the builder class
        builderClassBuilder.addMethod(MethodSpec
                .methodBuilder(this.builderFactoryMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(this.builderClassTypeParameters())
                .returns(builderFactoryMethodReturnType())
                .addStatement("return new $T()", this.builderClassTypeName())
                .build());

        for (VariableElement attribute : attributes) {
            String fieldName = attributeSimpleName(attribute);
            TypeName fieldType = TypeName.get(attribute.asType());

            builderClassBuilder.addField(FieldSpec
                    .builder(
                            fieldType,
                            fieldName,
                            Modifier.PRIVATE)
                    .build());

            List<MethodSpec> builderSetterMethods = this.generateBuilderSetterMethods(attribute);
            for (MethodSpec setterMethod : builderSetterMethods) {
                builderClassBuilder.addMethod(setterMethod);
            }
        }

        // add the 'build' method
        MethodSpec.Builder buildMethod = MethodSpec
                .methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(targetClassTypeName());
        String attributes = Utils.join(attributeNames());
        if (this.targetFactoryMethod == null) {
            buildMethod.addStatement("return new $T($L)", targetClassTypeName(), attributes);
        } else {
            buildMethod.addStatement("return $T.$L($L)",
                    // using ClassName gets rid of any type parameters the class might have
                    ClassName.get((TypeElement) this.targetFactoryMethod.getEnclosingElement()),
                    this.targetFactoryMethod.getSimpleName(), attributes);
        }
        builderClassBuilder.addMethod(buildMethod.build());

        enhance(builderClassBuilder);

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage(), builderClassBuilder.build())
                .build();
        javaFile.writeTo(filer);
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

    private List<MethodSpec> generateBuilderSetterMethods(VariableElement attribute) {
        return this.generateSetterMethods(attribute, /* mangleTypeParameters */ false,
                /* abstractMethod */ false);
    }

    protected final List<MethodSpec> generateSetterMethods(VariableElement attribute, boolean mangleTypeParameters,
            boolean abstractMethod) {
        String fieldName = this.attributeSimpleName(attribute);
        TypeName parameterType = this.attributeType(attribute, mangleTypeParameters);
        MethodSpec.Builder setter = MethodSpec
                .methodBuilder(this.setterMethodName(attribute))
                .addModifiers(Modifier.PUBLIC)
                .returns(this.returnTypeForSetterFor(attribute, mangleTypeParameters))
                .addParameter(this.setterParameterSpec(attribute, parameterType));
        if (abstractMethod) {
            setter.addModifiers(Modifier.ABSTRACT);
        } else {
            setter.addStatement("this.$1L = $1L", fieldName)
                    .addStatement("return this");
        }

        return Collections.singletonList(setter.build());
    }

    protected abstract TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters);

    private TypeName attributeType(VariableElement attribute,
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

    private ParameterSpec setterParameterSpec(VariableElement attribute, TypeName parameterType) {
        ParameterSpec.Builder ret = ParameterSpec.builder(parameterType,
                this.attributeSimpleName(attribute));
        AnnotationMirror nullableAnnotation = this.firstAnnotationCalledNullable(attribute);
        if (nullableAnnotation != null) {
            ret.addAnnotation(AnnotationSpec.get(nullableAnnotation));
        }
        return ret.build();
    }

    private Set<VariableElement> initOptionalAttributes() {
        Set<VariableElement> ret = new HashSet<VariableElement>();
        for (VariableElement attribute : attributes) {
            if (this.determineIfAttributeIsOptional(attribute)) {
                ret.add(attribute);
            }
        }
        return ret;
    }

    private boolean determineIfAttributeIsOptional(VariableElement attribute) {
        if (attribute.getAnnotation(Opt.class) != null) {
            return true;
        }
        if (this.firstAnnotationCalledNullable(attribute) != null) {
            return true;
        }
        return false;
    }

    private AnnotationMirror firstAnnotationCalledNullable(VariableElement attribute) {
        for (AnnotationMirror annotation : attribute.getAnnotationMirrors()) {
            if (annotationIsCalledNullable(annotation)) {
                return annotation;
            }
        }
        return null;
    }

    private static boolean annotationIsCalledNullable(AnnotationMirror annotation) {
        return "Nullable".equals(annotation.getAnnotationType().asElement().getSimpleName().toString());
    }

    private String initBuilderClassPackage() {
        String annotationBuilderPackageName = builderAnnotation.packageName();
        if (annotationBuilderPackageName.isEmpty()) {
            PackageElement targetClassPackage = elements.getPackageOf(targetClassType);
            return targetClassPackage.getQualifiedName().toString();
        } else {
            return annotationBuilderPackageName;
        }
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

    private Name targetClassSimpleName() {
        return this.targetClassType.getSimpleName();
    }

    protected final TypeName targetClassTypeName() {
        return this.targetFactoryMethod == null
                ? TypeName.get(this.targetClassType.asType())
                : TypeName.get(this.targetFactoryMethod.getReturnType());
    }

    protected final List<? extends VariableElement> attributes() {
        return attributes;
    }

    protected final boolean isOptional(VariableElement attribute) {
        return optionalAttributes.contains(attribute);
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

    protected List<TypeVariableName> builderClassTypeParameters() {
        List<? extends TypeParameterElement> typeParameterElements = this.targetFactoryMethod == null
                ? this.targetClassType.getTypeParameters()
                : this.targetFactoryMethod.getTypeParameters();
        List<TypeVariableName> ret = new ArrayList<TypeVariableName>(
                typeParameterElements.size());
        for (TypeParameterElement typeParameterEl : typeParameterElements) {
            ret.add(TypeVariableName.get(typeParameterEl));
        }
        return ret;
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

    protected final String builderFactoryMethodName() {
        String annotationFactoryMethod = builderAnnotation.factoryMethod();
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

    protected final AnnotationSpec generatedAnnotation() throws Exception {
        Class<?> generatedAnnotationClass = determineGeneratedAnnotationClass();
        return AnnotationSpec
                .builder(generatedAnnotationClass)
                .addMember("value", "$S", "Jilt-1.3")
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
