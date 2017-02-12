package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import java.util.Map;

class TypeSafeBuilderGenerator extends AbstractBuilderGenerator {
    TypeSafeBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        super(targetClass, elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() throws Exception {
        String interfacesName = targetClassType().getSimpleName() + "Builders";
        TypeSpec.Builder interfacesBuilder = TypeSpec.interfaceBuilder(interfacesName)
                .addModifiers(Modifier.PUBLIC);

        for (Map.Entry<String, Element> fieldEntry : fields().entrySet()) {
            String fieldName = fieldEntry.getKey();
            Element field = fieldEntry.getValue();
            TypeSpec.Builder interfaceBuilder = TypeSpec
                    .interfaceBuilder(interfaceNameForField(field))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC);

            Element nextField = next(fieldName);
            if (nextField != null) {
                interfaceBuilder.addMethod(MethodSpec.methodBuilder(fieldName)
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(ClassName.get(
                                builderClassPackage(),
                                interfacesName,
                                interfaceNameForField(nextField)))
                        .build());
            }

            interfacesBuilder.addType(interfaceBuilder.build());

        }

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage(), interfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    private String interfaceNameForField(Element field) {
        return Utils.capitalize(field.getSimpleName().toString());
    }

    private Element next(String fieldName) {
        boolean fieldFound = false;
        for (Map.Entry<String, Element> fieldEntry : fields().entrySet()) {
            if (fieldFound) {
                return fieldEntry.getValue();
            } else if (fieldName.equals(fieldEntry.getKey())) {
                fieldFound = true;
            }
        }
        return null;
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        return builderClassBuilder;
    }
}
