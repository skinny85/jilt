package org.jilt.test.data.non_repeatable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.test.data.annotations.NonParameterAnnotation;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class NonRepeatableAnnotationsName {
    @NonRepeatableAnnotation
    public String firstName;

    public NonRepeatableAnnotationsName(String firstName) {
        this.firstName = firstName;
    }
}
