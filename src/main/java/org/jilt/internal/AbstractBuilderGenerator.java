package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;
import org.jilt.Opt;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

abstract class AbstractBuilderGenerator implements BuilderGenerator {
    private final Elements elements;
    private final Filer filer;

    private final TypeElement targetClassType;
    private final List<VariableElement> fields;
    private final Set<VariableElement> optionalProperties;

    private final String builderClassPackage;
    private final ClassName builderClassTypeName;

    AbstractBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        if (targetClass.getKind() != ElementKind.CLASS) {
            throw new IllegalArgumentException(format(
                    "Only classes can be annotated with @%s",
                    Builder.class.getName()));
        }
        this.elements = elements;
        this.filer = filer;

        Builder builderAnnotation = targetClass.getAnnotation(Builder.class);

        this.targetClassType = (TypeElement) targetClass;
        this.fields = initFields();
        this.optionalProperties = initOptionalProperties();

        builderClassPackage = elements.getPackageOf(targetClassType).toString();
        String builderClassStringName = targetClassType.getSimpleName() + "Builder";
        builderClassTypeName = ClassName.get(builderClassPackage, builderClassStringName);
    }

    @Override
    public final void generateBuilderClass() throws Exception {
        generateClassesNeededByBuilder();

        // builder class
        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassTypeName)
                .addModifiers(Modifier.PUBLIC);

        // add a static factory method to the builder class
        builderClassBuilder.addMethod(MethodSpec
                .methodBuilder(factoryMethodName())
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(factoryMethodReturnType())
                .addStatement("return new $T()", builderClassTypeName())
                .build());

        for (VariableElement field : fields) {
            String fieldName = fieldSimpleName(field);
            TypeName fieldType = TypeName.get(field.asType());

            builderClassBuilder.addField(FieldSpec
                    .builder(
                            fieldType,
                            fieldName,
                            Modifier.PRIVATE)
                    .build());

            builderClassBuilder.addMethod(MethodSpec
                    .methodBuilder(builderSetterMethodName(field))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnTypeForSetterFor(field))
                    .addParameter(fieldType, fieldName)
                    .addStatement("this.$1L = $1L", fieldName)
                    .addStatement("return this")
                    .build());
        }

        TypeName targetClassName = targetClassTypeName();
        builderClassBuilder.addMethod(MethodSpec.methodBuilder(buildMethodName())
                .addModifiers(Modifier.PUBLIC)
                .returns(targetClassName)
                .addStatement("return new $T($L)", targetClassName, Utils.join(fieldNames()))
                .build());

        enhance(builderClassBuilder);

        JavaFile javaFile = JavaFile
                .builder(builderClassPackage, builderClassBuilder.build())
                .build();
        javaFile.writeTo(filer);
    }

    private List<String> fieldNames() {
        List<String> ret = new ArrayList<String>(fields.size());
        for (VariableElement field : fields) {
            ret.add(fieldSimpleName(field));
        }
        return ret;
    }

    protected abstract void generateClassesNeededByBuilder() throws Exception;

    protected abstract TypeName factoryMethodReturnType();

    protected abstract TypeName returnTypeForSetterFor(VariableElement field);

    protected abstract void enhance(TypeSpec.Builder builderClassBuilder);

    private List<VariableElement> initFields() {
        List<? extends Element> enclosedElements = targetClassType.getEnclosedElements();
        List<VariableElement> ret = new ArrayList<VariableElement>(enclosedElements.size());
        for (Element field : enclosedElements) {
            if (field.getKind() == ElementKind.FIELD &&
                    !field.getModifiers().contains(Modifier.STATIC) &&
                    field.getAnnotation(Builder.Ignore.class) == null)
                ret.add((VariableElement) field);
        }
        return ret;
    }

    private Set<VariableElement> initOptionalProperties() {
        Set<VariableElement> ret = new HashSet<VariableElement>();
        for (VariableElement field : fields) {
            if (field.getAnnotation(Opt.class) != null)
                ret.add(field);
        }
        return ret;
    }

    protected final Filer filer() {
        return filer;
    }

    protected final TypeElement targetClassType() {
        return targetClassType;
    }

    protected TypeName targetClassTypeName() {
        return TypeName.get(targetClassType.asType());
    }

    protected final List<VariableElement> fields() {
        return fields;
    }

    protected final boolean isOptional(VariableElement field) {
        return optionalProperties.contains(field);
    }

    protected final String builderClassPackage() {
        return builderClassPackage;
    }

    protected final TypeName builderClassTypeName() {
        return builderClassTypeName;
    }

    protected final String fieldSimpleName(VariableElement field) {
        return field.getSimpleName().toString();
    }

    protected final String builderSetterMethodName(VariableElement field) {
        return fieldSimpleName(field);
    }

    protected final String factoryMethodName() {
        return Utils.deCapitalize(targetClassType().getSimpleName().toString());
    }

    protected final String buildMethodName() {
        return "build";
    }
}
