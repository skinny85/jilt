package org.jilt.internal;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

public final class BuilderGeneratorFactory {
    private final Filer filer;
    private final Elements elements;

    public BuilderGeneratorFactory(Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
    }

    public BuilderGenerator forElement(Element annotatedElement) throws Exception {
        TypeElement targetClass;
        List<? extends VariableElement> attributes;
        TypeElement targetFactoryClass = null;
        Name targetFactoryMethod = null;

        ElementKind kind = annotatedElement.getKind();
        if (kind == ElementKind.CLASS) {
            targetClass = (TypeElement) annotatedElement;
            List<? extends Element> enclosedElements = targetClass.getEnclosedElements();
            List<VariableElement> fields = new ArrayList<VariableElement>(enclosedElements.size());
            for (Element field : enclosedElements) {
                if (field.getKind() == ElementKind.FIELD &&
                        !field.getModifiers().contains(Modifier.STATIC) &&
                        field.getAnnotation(Builder.Ignore.class) == null)
                    fields.add((VariableElement) field);
            }
            attributes = fields;
        } else if (kind == ElementKind.CONSTRUCTOR) {
            targetClass = (TypeElement) annotatedElement.getEnclosingElement();
            ExecutableElement constructor = (ExecutableElement) annotatedElement;
            attributes = constructor.getParameters();
        } else if (kind == ElementKind.METHOD &&
                annotatedElement.getModifiers().contains(Modifier.STATIC)) {
            ExecutableElement method = (ExecutableElement) annotatedElement;
            targetClass = (TypeElement) ((DeclaredType) method.getReturnType()).asElement();
            attributes = method.getParameters();
            targetFactoryClass = (TypeElement) method.getEnclosingElement();
            targetFactoryMethod = method.getSimpleName();
        } else {
            throw new IllegalArgumentException(
                    "@Builder can only be placed on classes, constructors or static methods");
        }

        Builder builderAnnotation = annotatedElement.getAnnotation(Builder.class);
        return builderAnnotation.style() == BuilderStyle.TYPE_SAFE
                ? new TypeSafeBuilderGenerator(targetClass, attributes, targetFactoryClass, targetFactoryMethod, elements, filer)
                : new ClassicBuilderGenerator(targetClass, attributes, targetFactoryClass, targetFactoryMethod, elements, filer);
    }
}
