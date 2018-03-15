package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

abstract class AbstractTypeSafeBuilderGenerator extends AbstractBuilderGenerator {
    private final BuilderInterfaces builderInterfaces;

    AbstractTypeSafeBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, BuilderInterfaces builderInterfaces, TypeElement targetFactoryClass,
            Name targetFactoryMethod, Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, targetFactoryClass, targetFactoryMethod, elements, filer);
        this.builderInterfaces = builderInterfaces;
    }

    @Override
    protected final void enhance(TypeSpec.Builder builderClassBuilder) {
        addSuperInterfaces(builderClassBuilder);

        builderClassBuilder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());
    }

    protected abstract void addSuperInterfaces(TypeSpec.Builder builderClassBuilder);

    protected final String outerInterfacesName() {
        String nameFromAnnotation = builderInterfaces == null
                ? ""
                : builderInterfaces.outerName();
        return nameFromAnnotation.isEmpty()
                ? targetClassType().getSimpleName() + "Builders"
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
        String capAttrName = Utils.capitalize(attributeSimpleName(attribute));

        String namesPattern = builderInterfaces == null
                ? ""
                : builderInterfaces.innerNames();

        return namesPattern.isEmpty()
                ? capAttrName
                : namesPattern.replaceAll("\\*", capAttrName);
    }

    protected final void addBuildMethodToInterface(TypeSpec.Builder interfaceBuilder) {
        interfaceBuilder.addMethod(MethodSpec
                .methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(targetClassTypeName())
                .build());
    }

    protected final ClassName innerInterfaceNamed(String interfaceName) {
        return ClassName.get(outerInterfacesPackage(), outerInterfacesName(), interfaceName);
    }

    protected final VariableElement nextAttribute(VariableElement attribute) {
        return nextAttribute(attributes().indexOf(attribute));
    }

    protected final VariableElement nextAttribute(int index) {
        int i = index + 1;
        return i < attributes().size() ? attributes().get(i) : null;
    }
}
