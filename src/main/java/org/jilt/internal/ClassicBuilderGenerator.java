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

class ClassicBuilderGenerator extends AbstractBuilderGenerator {
    private final Element targetClass;
    private final Elements elements;
    private final Filer filer;

    public ClassicBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        this.targetClass = targetClass;
        this.elements = elements;
        this.filer = filer;
    }

    @Override
    public void generateBuilderClass() throws Exception {
        if (targetClass.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(format(
                    "Only classes can be annotated with @%s",
                    Builder.class.getName()));
        }

        TypeElement targetClassType = (TypeElement) targetClass;
        String builderClassName = targetClassType.getSimpleName() + "Builder";
        String builderClassPackage = elements.getPackageOf(targetClassType).toString();
        ClassName builderClass = ClassName.get(builderClassPackage, builderClassName);

        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC);

        List<String> fields = new LinkedList<String>();
        for (Element field : targetClassType.getEnclosedElements()) {
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

        TypeName targetClassName = TypeName.get(targetClassType.asType());
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
