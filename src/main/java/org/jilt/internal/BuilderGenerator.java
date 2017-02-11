package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
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
        String builderClassPackage = elements.getPackageOf(typeElement).toString();
        ClassName builderClass = ClassName.get(builderClassPackage, builderClassName);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC);

        for (Element field : typeElement.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD)
                continue;

            String fieldName = field.getSimpleName().toString();
            TypeName fieldType = TypeName.get(field.asType());

            classBuilder.addField(FieldSpec
                    .builder(
                            fieldType,
                            fieldName,
                            Modifier.PRIVATE)
                    .build());

            MethodSpec setterMethod = MethodSpec.methodBuilder(fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(builderClass)
                    .addParameter(fieldType, "value")
                    .addStatement("this.$L = value", fieldName)
                    .addStatement("return this")
                    .build();

            classBuilder.addMethod(setterMethod);
        }

        TypeSpec typeSpec = classBuilder.build();

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage, typeSpec)
                .build();

        javaFile.writeTo(filer);
    }
}
