package org.jilt.internal;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
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
        ALLOWED_TYPE_KINDS = new HashSet<String>(2);
        ALLOWED_TYPE_KINDS.add(ElementKind.CLASS.name());
        // we don't want to use ElementKind.RECORD because it is not available in Java versions before 16
        ALLOWED_TYPE_KINDS.add("RECORD");
    }

    private final Filer filer;
    private final Elements elements;
    private final LazyTrees trees;

    public BuilderGeneratorFactory(Filer filer, Elements elements, LazyTrees trees) {
        this.filer = filer;
        this.elements = elements;
        this.trees = trees;
    }

    public BuilderGenerator forElement(Element annotatedElement, RoundEnvironment roundEnv) throws Exception {
        return annotatedElement.getKind() == ElementKind.ANNOTATION_TYPE
                ? this.new MetaAnnotationBuilderGenerator((TypeElement) annotatedElement, roundEnv)
                : this.forNonAnnotationElement(annotatedElement, annotatedElement.getAnnotation(Builder.class),
                     annotatedElement.getAnnotation(BuilderInterfaces.class));
    }

    private final class MetaAnnotationBuilderGenerator implements BuilderGenerator {
        private final TypeElement metaAnnotation;
        private final RoundEnvironment roundEnv;
        private final Builder builderAnnotation;
        private final BuilderInterfaces builderInterfaces;

        public MetaAnnotationBuilderGenerator(TypeElement metaAnnotation, RoundEnvironment roundEnv) {
            this.metaAnnotation = metaAnnotation;
            this.roundEnv = roundEnv;
            this.builderAnnotation = metaAnnotation.getAnnotation(Builder.class);
            this.builderInterfaces = metaAnnotation.getAnnotation(BuilderInterfaces.class);
        }

        @Override
        public void generateBuilderClass() throws Exception {
            for (Element annotatedElement : this.roundEnv.getElementsAnnotatedWith(this.metaAnnotation)) {
                BuilderGeneratorFactory.this.forNonAnnotationElement(annotatedElement,
                        this.builderAnnotation, this.builderInterfaces).generateBuilderClass();
            }
        }
    }

    private BuilderGenerator forNonAnnotationElement(Element annotatedElement, Builder builderAnnotation,
            BuilderInterfaces builderInterfaces) throws Exception {
        TypeElement targetClass;
        List<? extends VariableElement> attributes;
        ExecutableElement targetCreationMethod;

        ElementKind kind = annotatedElement.getKind();
        if (this.kindIsClassOrRecord(kind)) {
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
            targetCreationMethod = null;
        } else if (kind == ElementKind.CONSTRUCTOR) {
            targetClass = (TypeElement) annotatedElement.getEnclosingElement();
            ExecutableElement constructor = (ExecutableElement) annotatedElement;
            attributes = constructor.getParameters();
            targetCreationMethod = constructor;
        } else if (kind == ElementKind.METHOD &&
                annotatedElement.getModifiers().contains(Modifier.STATIC)) {
            ExecutableElement method = (ExecutableElement) annotatedElement;
            targetClass = (TypeElement) ((DeclaredType) method.getReturnType()).asElement();
            attributes = method.getParameters();
            targetCreationMethod = method;
        } else {
            throw new IllegalArgumentException(
                    "@Builder can only be placed on classes/records, constructors or static methods");
        }

        switch (builderAnnotation.style()) {
            case STAGED:
            case TYPE_SAFE:
                return new TypeSafeBuilderGenerator(targetClass, attributes, builderAnnotation,
                        builderInterfaces, targetCreationMethod, elements, trees, filer);
            case STAGED_PRESERVING_ORDER:
            case TYPE_SAFE_UNGROUPED_OPTIONALS:
                return new TypeSafeUngroupedOptionalsBuilderGenerator(targetClass, attributes, builderAnnotation,
                        builderInterfaces, targetCreationMethod, elements, trees, filer);
            case FUNCTIONAL:
                return new FunctionalBuilderGenerator(targetClass, attributes, builderAnnotation,
                        builderInterfaces, targetCreationMethod, elements, trees, filer);
            case CLASSIC:
            default:
                return new ClassicBuilderGenerator(targetClass, attributes, builderAnnotation,
                        targetCreationMethod, elements, trees, filer);
        }
    }

    private boolean kindIsClassOrRecord(ElementKind kind) {
        return ALLOWED_TYPE_KINDS.contains(kind.name());
    }
}
