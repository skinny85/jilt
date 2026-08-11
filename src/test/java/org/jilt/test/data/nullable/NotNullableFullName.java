package org.jilt.test.data.nullable;

import javax.annotation.Nullable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

public final class NotNullableFullName {
    public final String firstName, middleName, lastName;

    @Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER, nullableIsOptional = false)
    public NotNullableFullName(String firstName, @Nullable String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
