package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

class TypeSafeBuilderGenerator extends AbstractBuilderGenerator {
    private final String outerInterfacesName;
    private final String finalInterfaceName;

    TypeSafeBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        super(targetClass, elements, filer);
        outerInterfacesName = targetClassType().getSimpleName() + "Builders";
        finalInterfaceName = "Build";
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        TypeSpec.Builder outerInterfacesBuilder = TypeSpec.interfaceBuilder(outerInterfacesName)
                .addModifiers(Modifier.PUBLIC);

        for (int i = 0; i < fields().size(); i++) {
            VariableElement field = fields().get(i);
            VariableElement nextField = nextField(i);

            TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceNameForField(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            do {
                innerInterfaceBuilder.addMethod(MethodSpec
                        .methodBuilder(builderSetterMethodName(field))
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(ClassName.get(
                                outerInterfacesPackage(),
                                outerInterfacesName,
                                nextField == null ? finalInterfaceName : interfaceNameForField(nextField)))
                        .addParameter(TypeName.get(field.asType()), fieldSimpleName(field))
                        .build());

                if (nextField == null && isOptional(field)) {
                    addBuildMethodToInterface(innerInterfaceBuilder);
                }
            } while (nextField != null
                    && isOptional(field)
                    && (field = nextField) != null
                    && Utils.truth(nextField = nextField(field)));

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
    protected TypeName factoryMethodReturnType() {
        return firstInnerInterface();
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement field) {
        VariableElement nextField = nextField(field);
        String returnTypeName = nextField == null ? finalInterfaceName : interfaceNameForField(nextField);
        return ClassName.get(outerInterfacesPackage(), outerInterfacesName, returnTypeName);
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        TypeName firstInnerInterface = firstInnerInterface();

        builderClassBuilder.addSuperinterface(firstInnerInterface);
        for (VariableElement field : fields()) {
            builderClassBuilder.addSuperinterface(returnTypeForSetterFor(field));
        }

        builderClassBuilder.addMethod(MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .build());

        return builderClassBuilder;
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
                fields().isEmpty()
                        ? finalInterfaceName
                        : interfaceNameForField(fields().get(0)));
    }

    private String outerInterfacesPackage() {
        return builderClassPackage();
    }

    private String interfaceNameForField(VariableElement field) {
        return Utils.capitalize(fieldSimpleName(field));
    }

    private VariableElement nextField(VariableElement field) {
        return nextField(fields().indexOf(field));
    }

    private VariableElement nextField(int index) {
        int i = index + 1;
        return i < fields().size() ? fields().get(i) : null;
    }
}
