package org.jilt.internal;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class TypeSafeBuilderGenerator extends AbstractTypeSafeBuilderGenerator {
    private final String optionalsInterfaceName;

    TypeSafeBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
                             Builder builderAnnotation, TypeElement targetFactoryClass, Name targetFactoryMethod,
                             Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, targetFactoryClass, targetFactoryMethod, elements, filer);
        this.optionalsInterfaceName = "Optionals";
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        TypeSpec.Builder outerInterfacesBuilder = TypeSpec.interfaceBuilder(outerInterfacesName())
                .addModifiers(Modifier.PUBLIC);

        TypeSpec.Builder optionalsInterfaceBuilder = TypeSpec.interfaceBuilder(optionalsInterfaceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        for (VariableElement currentAttribute : attributes()) {
            MethodSpec setterMethod = MethodSpec
                    .methodBuilder(builderSetterMethodName(currentAttribute))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(returnTypeForSetterFor(currentAttribute))
                    .addParameter(TypeName.get(currentAttribute.asType()), attributeSimpleName(currentAttribute))
                    .build();

            if (isOptional(currentAttribute)) {
                optionalsInterfaceBuilder.addMethod(setterMethod);
            } else {
                TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                        .interfaceBuilder(interfaceNameForAttribute(currentAttribute))
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

                innerInterfaceBuilder.addMethod(setterMethod);

                outerInterfacesBuilder.addType(innerInterfaceBuilder.build());
            }
        }

        addBuildMethodToInterface(optionalsInterfaceBuilder);
        outerInterfacesBuilder.addType(optionalsInterfaceBuilder.build());

        JavaFile javaFile = JavaFile
                .builder(outerInterfacesPackage(), outerInterfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        VariableElement firstRequiredAttribute = firstRequiredAttribute();
        String returnTypeName = firstRequiredAttribute == null
                ? optionalsInterfaceName
                : interfaceNameForAttribute(firstRequiredAttribute);
        return innerInterfaceNamed(returnTypeName);
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute) {
        String returnTypeName;
        if (isOptional(attribute)) {
            returnTypeName = optionalsInterfaceName;
        } else {
            VariableElement nextRequiredAttribute = nextRequiredAttribute(attribute);
            returnTypeName = nextRequiredAttribute == null
                    ? optionalsInterfaceName
                    : interfaceNameForAttribute(nextRequiredAttribute);
        }
        return innerInterfaceNamed(returnTypeName);
    }

    @Override
    protected void addSuperInterfaces(TypeSpec.Builder builderClassBuilder) {
        for (VariableElement attribute : attributes()) {
            if (!isOptional(attribute))
                builderClassBuilder.addSuperinterface(innerInterfaceNamed(interfaceNameForAttribute(attribute)));
        }
        builderClassBuilder.addSuperinterface(innerInterfaceNamed(optionalsInterfaceName));
    }

    private VariableElement firstRequiredAttribute() {
        VariableElement ret = null;
        if (!attributes().isEmpty()) {
            VariableElement firstAttribute = attributes().get(0);
            ret = isOptional(firstAttribute) ? nextRequiredAttribute(firstAttribute) : firstAttribute;
        }
        return ret;
    }

    private VariableElement nextRequiredAttribute(VariableElement attribute) {
        VariableElement ret = attribute;

        do {
            ret = nextAttribute(ret);
        } while (ret != null && isOptional(ret));

        return ret;
    }
}
