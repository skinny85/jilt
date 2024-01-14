package org.jilt.internal;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.jilt.Builder;

import java.util.List;
import javax.annotation.processing.Filer;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

final class ClassicBuilderGenerator extends AbstractBuilderGenerator {
    ClassicBuilderGenerator(TypeElement targetClass, List<? extends VariableElement> attributes,
                            Builder builderAnnotation, ExecutableElement targetFactoryName,
                            Elements elements, Filer filer) {
        super(targetClass, attributes, builderAnnotation, targetFactoryName, elements, filer);
    }

    @Override
    protected void generateClassesNeededByBuilder() {
    }

    @Override
    protected TypeName builderFactoryMethodReturnType() {
        return builderClassTypeName();
    }

    @Override
    protected TypeName returnTypeForSetterFor(VariableElement attribute, boolean withMangledTypeParameters) {
        return builderClassTypeName();
    }

    @Override
    protected void enhance(TypeSpec.Builder builderClassBuilder) {
    }
}
