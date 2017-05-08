package org.jilt.internal;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

public class BuilderGeneratorFactory {
    private final Filer filer;
    private final Elements elements;

    public BuilderGeneratorFactory(Filer filer, Elements elements) {
        this.filer = filer;
        this.elements = elements;
    }

    public BuilderGenerator forClass(Element targetClass) throws Exception {
        Builder builderAnnotation = targetClass.getAnnotation(Builder.class);
        return builderAnnotation.style() == BuilderStyle.TYPE_SAFE
                ? new TypeSafeBuilderGenerator(targetClass, elements, filer)
                : new ClassicBuilderGenerator(targetClass, elements, filer);
    }
}
