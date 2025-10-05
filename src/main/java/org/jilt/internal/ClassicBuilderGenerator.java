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
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class ClassicBuilderGenerator extends AbstractBuilderGenerator {
    ClassicBuilderGenerator(Element annotatedElement, TypeElement targetClass, List<? extends VariableElement> attributes,
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
        List<MethodSpec> setterMethods = super.generateSetterMethods(attribute, mangleTypeParameters, abstractMethod);
        setterMethods.addAll(generateAddMethods(attribute));
        return setterMethods;
    }

    private List<MethodSpec> generateAddMethods(VariableElement attribute){
        boolean isSingular = false;
        String singularName = null;
        for (AnnotationMirror annotation : attribute.getAnnotationMirrors()) {
            // Chech that the annotation type is equal to the Singular class
            if (annotation.getAnnotationType().) { // TODO: Allow for lombok singular
                isSingular = true;
                Object value = annotation.getElementValues().values().stream().findFirst().orElse(null);
                singularName = value != null ? value.toString().replaceAll("\"", "") : null;
            }
        }

        if (isSingular && fieldType instanceof ParameterizedTypeName) {
            ParameterizedTypeName pType = (ParameterizedTypeName) fieldType;
            if (pType.rawType.toString().equals("java.util.List")) {
                // Generate accumulator field
                String accName = "_" + fieldName;
                builderClassBuilder.addField(FieldSpec.builder(fieldType, accName, Modifier.PRIVATE)
                        .initializer("new $T<>()", java.util.ArrayList.class)
                        .build());

                // Generate addX and addAllX methods
                String itemType = pType.typeArguments.get(0).toString();
                String singular = singularName != null && !singularName.isEmpty() ? singularName : (fieldName.endsWith("s") ? fieldName.substring(0, fieldName.length() - 1) : fieldName);
                String addOneName = "add" + Utils.capitalize(singular);
                String addAllName = "addAll" + Utils.capitalize(fieldName);

                builderClassBuilder.addMethod(MethodSpec.methodBuilder(addOneName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(this.builderClassTypeName())
                        .addParameter(pType.typeArguments.get(0), singular)
                        .addStatement("this.$L.add($L)", accName, singular)
                        .addStatement("return this")
                        .build());

                builderClassBuilder.addMethod(MethodSpec.methodBuilder(addAllName)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(this.builderClassTypeName())
                        .addParameter(ParameterizedTypeName.get(ClassName.get("java.util", "Collection"), WildcardTypeName.subtypeOf(pType.typeArguments.get(0))), fieldName)
                        .addStatement("this.$L.addAll($L)", accName, fieldName)
                        .addStatement("return this")
                        .build());

                // Do not generate normal setter for singular fields in classic builder
                continue;
            }
        }

    }

    private List<MethodSpec> generateAddMethods(){
        // todo
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
