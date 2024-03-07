package org.jilt.internal;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.utils.Annotations;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BuilderGeneratorFactory {
    private static final Set<String> ALLOWED_TYPE_KINDS;
    static {
        ALLOWED_TYPE_KINDS = new HashSet<>(2);
        ALLOWED_TYPE_KINDS.add(ElementKind.CLASS.name());
        // we don't want to use ElementKind.RECORD because it is not available in Java versions before 16
        ALLOWED_TYPE_KINDS.add("RECORD");
    }

    private final Filer filer;
    private final Elements elements;

    public BuilderGeneratorFactory(Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
    }

    public BuilderGenerator forElement(Element annotatedElement, Annotations annotations) throws Exception {
        TypeElement targetClass;
        List<? extends VariableElement> attributes;
        ExecutableElement targetFactoryMethod = null;

        ElementKind kind = annotatedElement.getKind();
        if (kindIsClassOrRecord(kind)) {
            targetClass = (TypeElement) annotatedElement;
            List<? extends Element> enclosedElements = targetClass.getEnclosedElements();
            List<VariableElement> fields = new ArrayList<>(enclosedElements.size());
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
            targetFactoryMethod = method;
        } else {
            throw new IllegalArgumentException(
                    "@Builder can only be placed on classes/records, constructors or static methods");
        }

        Builder builderAnnotation = annotations.getBuilder() == null ? annotatedElement.getAnnotation(Builder.class) : annotations.getBuilder();
        BuilderInterfaces builderInterfaces = annotations.getBuilderInterface() == null ? annotatedElement.getAnnotation(BuilderInterfaces.class) : annotations.getBuilderInterface();
        switch (builderAnnotation.style()) {
            case STAGED:
            case TYPE_SAFE:
                return new TypeSafeBuilderGenerator(targetClass, attributes, builderAnnotation,
                        builderInterfaces, targetFactoryMethod, elements, filer);
            case STAGED_PRESERVING_ORDER:
            case TYPE_SAFE_UNGROUPED_OPTIONALS:
                return new TypeSafeUngroupedOptionalsBuilderGenerator(targetClass, attributes, builderAnnotation,
                        builderInterfaces, targetFactoryMethod, elements, filer);
            case CLASSIC:
            default:
                return new ClassicBuilderGenerator(targetClass, attributes, builderAnnotation,
                        targetFactoryMethod, elements, filer);
        }
    }

    private boolean kindIsClassOrRecord(ElementKind kind) {
        return ALLOWED_TYPE_KINDS.contains(kind.name());
    }
}
