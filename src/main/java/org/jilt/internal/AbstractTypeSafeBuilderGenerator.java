package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.Opt;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractTypeSafeBuilderGenerator extends AbstractBuilderGenerator {
    private final Set<VariableElement> optionalAttributes;
    private final BuilderInterfaces builderInterfaces;

    AbstractTypeSafeBuilderGenerator(Element annotatedElement, TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, BuilderInterfaces builderInterfaces,
            ExecutableElement targetCreationMethod, Elements elements, Filer filer) {
        super(annotatedElement, targetClass, attributes, builderAnnotation, targetCreationMethod, elements, filer);
        this.optionalAttributes = this.initOptionalAttributes(attributes);
        this.builderInterfaces = builderInterfaces;
    }

    @Override
    protected final void enhance(TypeSpec.Builder builderClassBuilder) {
        addSuperInterfaces(builderClassBuilder);

        // only generate a private constructor if the builder class is not abstract
        // (if the constructor is private, the class wouldn't be able to be extended,
        // which defeats the purpose of an abstract class)
        if (!this.builderClassNeedsToBeAbstract()) {
            builderClassBuilder.addMethod(MethodSpec
                    .constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .build());
        }
    }

    protected abstract void addSuperInterfaces(TypeSpec.Builder builderClassBuilder);

    protected abstract String defaultLastInterfaceName();

    protected final String outerInterfacesName() {
        String nameFromAnnotation = builderInterfaces == null
                ? ""
                : builderInterfaces.outerName();
        return nameFromAnnotation.isEmpty()
                ? this.builderClassStringName() + "s"
                : nameFromAnnotation;
    }

    protected final String outerInterfacesPackage() {
        String packageFromAnnotation = builderInterfaces == null
                ? ""
                : builderInterfaces.packageName();
        return packageFromAnnotation.isEmpty()
                ? builderClassPackage()
                : packageFromAnnotation;
    }

    protected final String interfaceNameForAttribute(VariableElement attribute) {
        return interfaceNameFromBaseName(Utils.capitalize(attributeSimpleName(attribute)));
    }

    protected final boolean isOptional(VariableElement attribute) {
        return optionalAttributes.contains(attribute);
    }

    protected final MethodSpec generateInterfaceSetterMethod(VariableElement attribute,
            boolean mangleTypeParameters) {
        return this.generateSetterMethod(attribute, mangleTypeParameters,
                /* abstractMethod */ true);
    }

    protected final String lastInterfaceName() {
        String nameFromAnnotation = builderInterfaces == null
                ? ""
                : builderInterfaces.lastInnerName();

        return nameFromAnnotation.isEmpty()
                ? interfaceNameFromBaseName(defaultLastInterfaceName())
                : nameFromAnnotation;
    }

    protected final void addBuildMethodToInterface(TypeSpec.Builder interfaceBuilder,
            boolean withMangledTypeParameters) {
        TypeName targetClassTypeName = this.targetClassTypeName();
        TypeName buildReturnType = withMangledTypeParameters
                ? this.mangleTypeName(targetClassTypeName)
                : targetClassTypeName;
        interfaceBuilder.addMethod(MethodSpec
                .methodBuilder(this.buildMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addExceptions(this.targetCreationMethodThrownExceptions())
                .returns(buildReturnType)
                .build());
    }

    protected final TypeName innerInterfaceNamed(String interfaceName) {
        return this.innerInterfaceNamed(interfaceName, false);
    }

    protected final TypeName innerInterfaceNamed(String interfaceName, boolean withMangledTypeParameters) {
        List<TypeVariableName> typeVariableNames = withMangledTypeParameters
                ? this.mangledBuilderClassTypeParameters()
                : this.builderClassTypeParameters();
        ClassName innerInterfaceClassName = ClassName.get(outerInterfacesPackage(), outerInterfacesName(), interfaceName);
        return typeVariableNames.isEmpty()
                ? innerInterfaceClassName
                : ParameterizedTypeName.get(innerInterfaceClassName,
                    typeVariableNames.toArray(new TypeVariableName[0]));
    }

    protected final List<TypeVariableName> mangledBuilderClassTypeParameters() {
        // There's an interesting edge case. If the name of the property is the same as the type parameter
        // (so, class MyClass<T1, T2> { T1 t1; T2 t2; }), the name of the inner interface for that property will be the same as the name of the type parameter.
        // This will cause a conflict when the setter method's return type is simply T2,
        // because the T2 type variable shadows the T2 inner interface in that context,
        // and there's no way to force JavaPoet to output the T2 return type as qualified with the outer interface.
        // So, add an underscore suffix to the names of the type variables in the inner interfaces to resolve this conflict.
        List<TypeVariableName> typeVariableNames = this.builderClassTypeParameters();
        List<TypeVariableName> ret = new ArrayList<TypeVariableName>(typeVariableNames.size());
        for (TypeVariableName typeVariableName : typeVariableNames) {
            ret.add(this.mangleTypeParameter(typeVariableName));
        }
        return ret;
    }

    protected final VariableElement nextAttribute(VariableElement attribute) {
        return nextAttribute(attributes().indexOf(attribute));
    }

    protected final VariableElement nextAttribute(int index) {
        int i = index + 1;
        return i < attributes().size() ? attributes().get(i) : null;
    }

    protected final String interfaceNameFromBaseName(String baseName) {
        String namesPattern = builderInterfaces == null
                ? ""
                : builderInterfaces.innerNames();

        return namesPattern.isEmpty()
                ? baseName
                : namesPattern.replaceAll("\\*", baseName);
    }

    private Set<VariableElement> initOptionalAttributes(List<? extends VariableElement> attributes) {
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
        if (this.hasAnnotationCalledNullable(attribute)) {
            return true;
        }
        return false;
    }

    private boolean hasAnnotationCalledNullable(VariableElement attribute) {
        for (AnnotationMirror annotation : attribute.getAnnotationMirrors()) {
            if (annotationIsCalledNullable(annotation)) {
                return true;
            }
        }
        // some annotations are applied to the type, instead of the attribute,
        // like the ones from JSpecify
        for (AnnotationMirror annotation : attribute.asType().getAnnotationMirrors()) {
            if (annotationIsCalledNullable(annotation)) {
                return true;
            }
        }
        return false;
    }

    private static boolean annotationIsCalledNullable(AnnotationMirror annotation) {
        return "Nullable".equals(annotation.getAnnotationType().asElement().getSimpleName().toString());
    }
}
