package org.jilt.test.data.annotations;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.test.data.annotations.NonParameterAnnotation;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class NonRepeatableAnnotationsName {
    @NonRepeatableAnnotation
    public String name;

    public NonRepeatableAnnotationsName(String name) {
        this.name = name;
    }
}
