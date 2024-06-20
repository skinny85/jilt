package org.jilt.test.data.annotations;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public final class PropagateAnnotationsFullName {
    @FieldParamTypeUseAnnotation
    public final String firstName;

    @Nullable
    @Opt
    @RepeatableAnnotation(1)
    @RepeatableAnnotation(2)
    public final String middleName;

    @Nonnull
    @FieldOnlyAnnotation
    @RepeatableAnnotations({@RepeatableAnnotation(3), @RepeatableAnnotation(4)})
    public final String lastName;

    public PropagateAnnotationsFullName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
