package org.jilt.internal;

import com.squareup.javapoet.JavaFile;
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
        TypeSpec.Builder interfacesBuilder = TypeSpec.interfaceBuilder(targetClassType().getSimpleName() + "Builders")
                .addModifiers(Modifier.PUBLIC);

        for (Map.Entry<String, Element> fieldEntry : fields().entrySet()) {
            interfacesBuilder.addType(TypeSpec
                    .interfaceBuilder(Utils.capitalize(fieldEntry.getKey()))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build());
        }

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage(),  interfacesBuilder.build())
                .build();
        javaFile.writeTo(filer());
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        return builderClassBuilder;
    }
}
