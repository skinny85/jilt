package org.jilt.internal;

import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;

class TypeSafeBuilderGenerator extends AbstractBuilderGenerator {
    TypeSafeBuilderGenerator(Element targetClass, Elements elements, Filer filer) {
        super(targetClass, elements, filer);
    }

    @Override
    protected TypeSpec.Builder enhance(TypeSpec.Builder builderClassBuilder) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
