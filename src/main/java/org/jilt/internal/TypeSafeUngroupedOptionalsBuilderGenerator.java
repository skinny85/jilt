package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class TypeSafeUngroupedOptionalsBuilderGenerator extends AbstractBuilderGenerator {
    private final String outerInterfacesName;
    private final String finalInterfaceName;

    TypeSafeUngroupedOptionalsBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
                                               TypeElement targetFactoryClass, Name targetFactoryName,
                                               Elements elements, Filer filer) {
        super(targetClass, attributes, targetFactoryClass, targetFactoryName, elements, filer);
        outerInterfacesName = targetClassType().getSimpleName() + "Builders";
        finalInterfaceName = "Build";
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        TypeSpec.Builder outerInterfacesBuilder = TypeSpec.interfaceBuilder(outerInterfacesName)
                .addModifiers(Modifier.PUBLIC);

        for (int i = 0; i < attributes().size(); i++) {
            VariableElement currentAttribute = attributes().get(i);
            VariableElement nextAttribute = nextAttribute(i);

            TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceNameForAttribute(currentAttribute))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            do {
                innerInterfaceBuilder.addMethod(MethodSpec
                        .methodBuilder(builderSetterMethodName(currentAttribute))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(ClassName.get(
                                outerInterfacesPackage(),
                                outerInterfacesName,
                                nextAttribute == null ? finalInterfaceName : interfaceNameForAttribute(nextAttribute)))
                        .addParameter(TypeName.get(currentAttribute.asType()), attributeSimpleName(currentAttribute))
                        .build());

                if (nextAttribute == null && isOptional(currentAttribute)) {
                    addBuildMethodToInterface(innerInterfaceBuilder);
                }
            } while (nextAttribute != null
                    && isOptional(currentAttribute)
                    && (currentAttribute = nextAttribute) != null
                    && Utils.truth(nextAttribute = nextAttribute(currentAttribute)));

            outerInterfacesBuilder.addType(innerInterfaceBuilder.build());
        }
        TypeSpec.Builder finalInterfaceBuilder = TypeSpec
                .interfaceBuilder(finalInterfaceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        addBuildMethodToInterface(finalInterfaceBuilder);

        outerInterfacesBuilder.addType(finalInterfaceBuilder.build());

        JavaFile javaFile = JavaFile
                .builder(outerInterfacesPackage(), outerInterfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        return firstInnerInterface();
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute) {
        VariableElement nextAttribute = nextAttribute(attribute);
        String returnTypeName = nextAttribute == null ? finalInterfaceName : interfaceNameForAttribute(nextAttribute);
        return ClassName.get(outerInterfacesPackage(), outerInterfacesName, returnTypeName);
    }

    @Override
    protected void enhance(TypeSpec.Builder builderClassBuilder) {
        TypeName firstInnerInterface = firstInnerInterface();

        builderClassBuilder.addSuperinterface(firstInnerInterface);
        for (VariableElement attribute : attributes()) {
            builderClassBuilder.addSuperinterface(returnTypeForSetterFor(attribute));
        }

        builderClassBuilder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());
    }

    private void addBuildMethodToInterface(TypeSpec.Builder interfaceBuilder) {
        interfaceBuilder.addMethod(MethodSpec
                .methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(targetClassTypeName())
                .build());
    }

    private TypeName firstInnerInterface() {
        return ClassName.get(
                outerInterfacesPackage(),
                outerInterfacesName,
                attributes().isEmpty()
                        ? finalInterfaceName
                        : interfaceNameForAttribute(attributes().get(0)));
    }

    private String outerInterfacesPackage() {
        return builderClassPackage();
    }

    private String interfaceNameForAttribute(VariableElement attribute) {
        return Utils.capitalize(attributeSimpleName(attribute));
    }

    private VariableElement nextAttribute(VariableElement attribute) {
        return nextAttribute(attributes().indexOf(attribute));
    }

    private VariableElement nextAttribute(int index) {
        int i = index + 1;
        return i < attributes().size() ? attributes().get(i) : null;
    }
}
