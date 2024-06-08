package org.jilt.test.data.annotations;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class PropagatedAnnotationsFullName {
    @CheckForNull
    public String firstName;

    @Nullable
    public String middleName;

    @Nonnull
    @NonParameterAnnotation
    public String lastName;

    public PropagatedAnnotationsFullName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
