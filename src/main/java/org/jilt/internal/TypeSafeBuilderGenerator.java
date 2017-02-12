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
    TypeSafeBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        super(targetClass, elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        String interfacesName = targetClassType().getSimpleName() + "Builders";
        TypeSpec.Builder interfacesBuilder = TypeSpec.interfaceBuilder(interfacesName)
                .addModifiers(Modifier.PUBLIC);

        String finalInterfaceName = "Build";
        for (int i = 0; i < fields().size(); i++) {
            VariableElement field = fields().get(i);
            TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceNameForField(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            VariableElement nextField = nextField(i);
            innerInterfaceBuilder.addMethod(MethodSpec
                    .methodBuilder(builderSetterMethodName(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ClassName.get(
                            builderClassPackage(),
                            interfacesName,
                            nextField == null ? finalInterfaceName : interfaceNameForField(nextField)))
                    .addParameter(TypeName.get(field.asType()), fieldSimpleName(field))
                    .build());

            interfacesBuilder.addType(innerInterfaceBuilder.build());
        }
        TypeSpec.Builder finalInterfaceBuilder = TypeSpec
                .interfaceBuilder(finalInterfaceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        finalInterfaceBuilder.addMethod(MethodSpec
                .methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(targetClassTypeName())
                .build());

        interfacesBuilder.addType(finalInterfaceBuilder.build());

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage(), interfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    private String interfaceNameForField(VariableElement field) {
        return Utils.capitalize(fieldSimpleName(field));
    }

    private VariableElement nextField(int index) {
        int i = index + 1;
        return i < fields().size() ? fields().get(i) : null;
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        return builderClassBuilder;
    }
}
