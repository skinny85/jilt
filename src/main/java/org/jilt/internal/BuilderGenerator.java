package org.jilt.internal;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static java.lang.String.format;

public class BuilderGenerator {
    private final Elements elements;
    private final Filer filer;

    public BuilderGenerator(Elements elements, Filer filer) {
        this.elements = elements;
        this.filer = filer;
    }

    public void generateBuilderClass(Element annotatedElement) throws Exception {
        if (annotatedElement.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(format(
                    "Only classes can be annotated with @%s",
                    Builder.class.getName()));
        }

        TypeElement typeElement = (TypeElement) annotatedElement;
        String builderClassName = typeElement.getSimpleName() + "Builder";

        TypeSpec typeSpec = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile
                .builder(elements.getPackageOf(typeElement).toString(), typeSpec)
                .build();

        javaFile.writeTo(filer);
    }
}
