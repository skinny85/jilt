package org.jilt.internal;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import org.jilt.Builder;
import org.jilt.Singular;
import org.jilt.utils.Utils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import java.util.ArrayList;
import java.util.List;

final class ClassicBuilderGenerator extends AbstractBuilderGenerator {
    ClassicBuilderGenerator(Element annotatedElement, TypeElement targetClass,
                            List<? extends VariableElement> attributes,
                            Builder builderAnnotation, ExecutableElement targetCreationMethod,
                            Elements elements, Filer filer) {
        super(annotatedElement, targetClass, attributes, builderAnnotation, targetCreationMethod, elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() {
    }

    @Override
    protected List<MethodSpec> generateSetterMethods(VariableElement attribute,
                                                     boolean mangleTypeParameters, boolean abstractMethod) {
        List<MethodSpec> setterMethods = new ArrayList<>(super.generateSetterMethods(attribute, mangleTypeParameters,
                abstractMethod));
        if (!abstractMethod) {
            setterMethods.addAll(generateAddMethods(attribute));
        }
        return setterMethods;
    }

    private List<MethodSpec> generateAddMethods(VariableElement attribute) {
        Singular singularAnnotation = attribute.getAnnotation(Singular.class);
        if (singularAnnotation == null || !isList(attribute)) {
            return List.of();
        }

        TypeName attributeType = TypeName.get(attribute.asType());
        ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) attributeType;
        TypeName itemType = parameterizedTypeName.typeArguments.get(0);

        String fieldName = attributeSimpleName(attribute);
        String singularName = singularAnnotation.value();
        if (singularName.isEmpty()) {
            if (fieldName.endsWith("s")) {
                singularName = fieldName.substring(0, fieldName.length() - 1);
            } else {
                singularName = fieldName;
            }
        }

        List<MethodSpec> methods = new ArrayList<>();

        // add method
        MethodSpec.Builder addMethod = MethodSpec.methodBuilder("add" + Utils.capitalize(singularName))
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClassTypeName())
                .addParameter(itemType, singularName);

        addMethod.beginControlFlow("if (this.$L == null)", fieldName)
                .addStatement("this.$L = new $T<>()", fieldName, ClassName.get("java.util", "ArrayList"))
                .endControlFlow();
        addMethod.addStatement("this.$L.add($L)", fieldName, singularName)
                .addStatement("return this");
        methods.add(addMethod.build());

        // addAll method
        TypeName collectionOfItems = ParameterizedTypeName.get(
                ClassName.get("java.util", "Collection"),
                WildcardTypeName.subtypeOf(itemType)
        );

        MethodSpec.Builder addAllMethod = MethodSpec.methodBuilder("addAll" + Utils.capitalize(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClassTypeName())
                .addParameter(collectionOfItems, fieldName);

        addAllMethod.beginControlFlow("if (this.$L == null)", fieldName)
                .addStatement("this.$L = new $T<>()", fieldName, ClassName.get("java.util", "ArrayList"))
                .endControlFlow();
        addAllMethod.addStatement("this.$L.addAll($L)", fieldName, fieldName)
                .addStatement("return this");
        methods.add(addAllMethod.build());

        return methods;
    }

    private boolean isList(VariableElement attribute) {
        TypeName attributeType = TypeName.get(attribute.asType());
        if (!(attributeType instanceof ParameterizedTypeName)) {
            return false;
        }
        ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) attributeType;
        return parameterizedTypeName.rawType.toString().equals("java.util.List");
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        return builderClassTypeName();
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters) {
        return this.builderClassTypeName();
    }

    @Override
    protected void enhance(TypeSpec.Builder builderClassBuilder) {
    }
}