package org.jilt.internal;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.List;

final class ClassicBuilderGenerator extends AbstractBuilderGenerator {
    ClassicBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
                            Builder builderAnnotation, TypeElement targetFactoryClass, Name targetFactoryName,
                            Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, targetFactoryClass, targetFactoryName, elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() {
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        return builderClassTypeName();
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute) {
        return builderClassTypeName();
    }

    @Override
    protected void enhance(TypeSpec.Builder builderClassBuilder) {
    }
}
