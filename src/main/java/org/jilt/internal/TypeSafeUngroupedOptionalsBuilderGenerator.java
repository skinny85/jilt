package org.jilt.internal;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class TypeSafeUngroupedOptionalsBuilderGenerator extends AbstractTypeSafeBuilderGenerator {
    TypeSafeUngroupedOptionalsBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
            Builder builderAnnotation, BuilderInterfaces builderInterfaces,
            ExecutableElement targetFactoryMethod, Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, builderInterfaces, targetFactoryMethod,
                elements, filer);
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
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addTypeVariables(this.mangledBuilderClassTypeParameters());

            do {
                List<MethodSpec> setterMethods = this.generateInterfaceSetterMethods(currentAttribute, true);
                for (MethodSpec setterMethod : setterMethods) {
                    innerInterfaceBuilder.addMethod(setterMethod);
                }

                if (nextAttribute == null && isOptional(currentAttribute)) {
                    this.addBuildMethodToInterface(innerInterfaceBuilder,
                            /* withMangledTypeParameters */ true);
                }
            } while (nextAttribute != null
                    && isOptional(currentAttribute)
                    && (currentAttribute = nextAttribute) != null
                    && Utils.truth(nextAttribute = nextAttribute(currentAttribute)));

            outerInterfacesBuilder.addType(innerInterfaceBuilder.build());
        }

        TypeSpec.Builder finalInterfaceBuilder = TypeSpec
                .interfaceBuilder(lastInterfaceName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addTypeVariables(this.builderClassTypeParameters());
        this.addBuildMethodToInterface(finalInterfaceBuilder,
                /* withMangledTypeParameters */ false);
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
    protected TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters) {
        VariableElement nextAttribute = nextAttribute(attribute);
        String returnTypeName = nextAttribute == null
                ? lastInterfaceName()
                : interfaceNameForAttribute(nextAttribute);
        return this.innerInterfaceNamed(returnTypeName, withMangledTypeParameters);
    }

    @Override
    protected void addSuperInterfaces(TypeSpec.Builder builderClassBuilder) {
        TypeName firstInnerInterface = firstInnerInterface();

        builderClassBuilder.addSuperinterface(firstInnerInterface);
        for (VariableElement attribute : attributes()) {
            builderClassBuilder.addSuperinterface(this.returnTypeForSetterFor(attribute, false));
        }
    }

    private TypeName firstInnerInterface() {
        return innerInterfaceNamed(attributes().isEmpty()
                ? lastInterfaceName()
                : interfaceNameForAttribute(attributes().get(0)));
    }

    @Override
    protected String defaultLastInterfaceName() {
        return "Build";
    }
}
