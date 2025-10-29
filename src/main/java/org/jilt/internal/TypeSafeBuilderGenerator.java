package org.jilt.internal;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.TypeName;
import com.palantir.javapoet.TypeSpec;
import org.jilt.Builder;
import org.jilt.BuilderInterfaces;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class TypeSafeBuilderGenerator extends AbstractTypeSafeBuilderGenerator {
    TypeSafeBuilderGenerator(Element annotatedElement, TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, BuilderInterfaces builderInterfaces,
            ExecutableElement targetCreationMethod, Elements elements, Filer filer) {
        super(annotatedElement, targetClass, attributes, builderAnnotation, builderInterfaces, targetCreationMethod,
                elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        TypeSpec.Builder outerInterfacesBuilder = TypeSpec.interfaceBuilder(outerInterfacesName())
                .addAnnotation(generatedAnnotation())
                .addModifiers(Modifier.PUBLIC);

        TypeSpec.Builder optionalsInterfaceBuilder = TypeSpec.interfaceBuilder(lastInterfaceName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(this.builderClassTypeParameters());

        for (VariableElement currentAttribute : attributes()) {
            boolean attributeIsOptional = this.isOptional(currentAttribute);
            MethodSpec setterMethod = this.generateInterfaceSetterMethod(currentAttribute, !attributeIsOptional);
            if (attributeIsOptional) {
                optionalsInterfaceBuilder.addMethod(setterMethod);
            } else {
                TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                        .interfaceBuilder(interfaceNameForAttribute(currentAttribute))
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .addTypeVariables(this.mangledBuilderClassTypeParameters());
                innerInterfaceBuilder.addMethod(setterMethod);

                outerInterfacesBuilder.addType(innerInterfaceBuilder.build());
            }
        }

        this.addBuildMethodToInterface(optionalsInterfaceBuilder,
                /* withMangledTypeParameters */ false);
        outerInterfacesBuilder.addType(optionalsInterfaceBuilder.build());
        outerInterfacesBuilder.addOriginatingElement(this.annotatedElement);

        JavaFile javaFile = JavaFile
                .builder(outerInterfacesPackage(), outerInterfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        VariableElement firstRequiredAttribute = firstRequiredAttribute();
        String returnTypeName = firstRequiredAttribute == null
                ? lastInterfaceName()
                : interfaceNameForAttribute(firstRequiredAttribute);
        return innerInterfaceNamed(returnTypeName);
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters) {
        String returnTypeName;
        if (isOptional(attribute)) {
            returnTypeName = lastInterfaceName();
        } else {
            VariableElement nextRequiredAttribute = nextRequiredAttribute(attribute);
            returnTypeName = nextRequiredAttribute == null
                    ? lastInterfaceName()
                    : interfaceNameForAttribute(nextRequiredAttribute);
        }
        return this.innerInterfaceNamed(returnTypeName, withMangledTypeParameters);
    }

    @Override
    protected void addSuperInterfaces(TypeSpec.Builder builderClassBuilder) {
        for (VariableElement attribute : attributes()) {
            if (!isOptional(attribute))
                builderClassBuilder.addSuperinterface(innerInterfaceNamed(interfaceNameForAttribute(attribute)));
        }
        builderClassBuilder.addSuperinterface(innerInterfaceNamed(lastInterfaceName()));
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

    @Override
    protected String defaultLastInterfaceName() {
        return "Optionals";
    }
}
