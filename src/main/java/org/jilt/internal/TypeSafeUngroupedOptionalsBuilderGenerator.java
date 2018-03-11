package org.jilt.internal;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
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

final class TypeSafeUngroupedOptionalsBuilderGenerator extends AbstractTypeSafeBuilderGenerator {
    private final String finalInterfaceName;

    TypeSafeUngroupedOptionalsBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, BuilderInterfaces builderInterfaces, TypeElement targetFactoryClass,
            Name targetFactoryMethod, Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, builderInterfaces, targetFactoryClass, targetFactoryMethod,
                elements, filer);
        finalInterfaceName = "Build";
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        TypeSpec.Builder outerInterfacesBuilder = TypeSpec.interfaceBuilder(outerInterfacesName())
                .addAnnotation(generatedAnnotation())
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
                        .returns(returnTypeForSetterFor(currentAttribute))
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
        String returnTypeName = nextAttribute == null
                ? finalInterfaceName
                : interfaceNameForAttribute(nextAttribute);
        return innerInterfaceNamed(returnTypeName);
    }

    @Override
    protected void addSuperInterfaces(TypeSpec.Builder builderClassBuilder) {
        TypeName firstInnerInterface = firstInnerInterface();

        builderClassBuilder.addSuperinterface(firstInnerInterface);
        for (VariableElement attribute : attributes()) {
            builderClassBuilder.addSuperinterface(returnTypeForSetterFor(attribute));
        }
    }

    private TypeName firstInnerInterface() {
        return innerInterfaceNamed(attributes().isEmpty()
                ? finalInterfaceName
                : interfaceNameForAttribute(attributes().get(0)));
    }
}
