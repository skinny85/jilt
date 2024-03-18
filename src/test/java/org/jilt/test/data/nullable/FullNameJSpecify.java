package org.jilt.test.data.nullable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jspecify.annotations.Nullable;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public final class FullNameJSpecify {
    public final String firstName;

    @Nullable
    public final String middleName;

    public final String lastName;

    public FullNameJSpecify(String firstName, @Nullable String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
