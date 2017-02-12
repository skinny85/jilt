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
            TypeSpec.Builder innerInterfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceNameForField(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            VariableElement nextField = nextField(i);
            innerInterfaceBuilder.addMethod(MethodSpec
                    .methodBuilder(builderSetterMethodName(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(ClassName.get(
                            outerInterfacesPackage(),
                            outerInterfacesName,
                            nextField == null ? finalInterfaceName : interfaceNameForField(nextField)))
                    .addParameter(TypeName.get(field.asType()), fieldSimpleName(field))
                    .build());

            outerInterfacesBuilder.addType(innerInterfaceBuilder.build());
        }
        TypeSpec.Builder finalInterfaceBuilder = TypeSpec
                .interfaceBuilder(finalInterfaceName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

        finalInterfaceBuilder.addMethod(MethodSpec
                .methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(targetClassTypeName())
                .build());

        outerInterfacesBuilder.addType(finalInterfaceBuilder.build());

        JavaFile javaFile = JavaFile
                .builder(outerInterfacesPackage(), outerInterfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement field) {
        VariableElement nextField = nextField(field);
        String returnTypeName = nextField == null ? finalInterfaceName :  interfaceNameForField(nextField);
        return ClassName.get(outerInterfacesPackage(), outerInterfacesName, returnTypeName);
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        if (!fields().isEmpty()) {
            ClassName firstInnerInterface = ClassName.get(
                    outerInterfacesPackage(),
                    outerInterfacesName,
                    interfaceNameForField(fields().get(0)));

            builderClassBuilder.addSuperinterface(firstInnerInterface);

            builderClassBuilder.addMethod(MethodSpec
                    .methodBuilder(Utils.deCapitalize(targetClassType().getSimpleName().toString()))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(firstInnerInterface)
                    .addStatement("return new $T()", builderClassTypeName())
                    .build());

            builderClassBuilder.addMethod(MethodSpec
                    .constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .build());
        }

        for (VariableElement field : fields()) {
            builderClassBuilder.addSuperinterface(returnTypeForSetterFor(field));
        }

        return builderClassBuilder;
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
