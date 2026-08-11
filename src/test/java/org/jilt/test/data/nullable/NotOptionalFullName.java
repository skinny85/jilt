package org.jilt.test.data.nullable;

import javax.annotation.Nullable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

public final class NotOptionalFullName {
    public final String firstName, middleName, lastName;

    @Builder(style = BuilderStyle.STAGED, treatNullableAsOptional = false)
    public NotOptionalFullName(String firstName, @Nullable String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
