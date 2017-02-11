package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.LinkedList;
import java.util.List;

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

        TypeElement targetClass = (TypeElement) annotatedElement;
        String builderClassName = targetClass.getSimpleName() + "Builder";
        String builderClassPackage = elements.getPackageOf(targetClass).toString();
        ClassName builderClass = ClassName.get(builderClassPackage, builderClassName);

        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC);

        List<String> fields = new LinkedList<String>();
        for (Element field : targetClass.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD ||
                    field.getModifiers().contains(Modifier.STATIC))
                continue;

            String fieldName = field.getSimpleName().toString();
            TypeName fieldType = TypeName.get(field.asType());
            fields.add(fieldName);

            builderClassBuilder.addField(FieldSpec
                    .builder(
                            fieldType,
                            fieldName,
                            Modifier.PRIVATE)
                    .build());

            builderClassBuilder.addMethod(MethodSpec
                    .methodBuilder(fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(builderClass)
                    .addParameter(fieldType, "value")
                    .addStatement("this.$L = value", fieldName)
                    .addStatement("return this")
                    .build());
        }

        TypeName targetClassName = TypeName.get(targetClass.asType());
        builderClassBuilder.addMethod(MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(targetClassName)
                .addStatement("return new $T($L)", targetClassName, Utils.join(fields))
                .build());

        TypeSpec builderClassSpec = builderClassBuilder.build();
        JavaFile javaFile = JavaFile
                .builder(builderClassPackage, builderClassSpec)
                .build();
        javaFile.writeTo(filer);
    }
}
