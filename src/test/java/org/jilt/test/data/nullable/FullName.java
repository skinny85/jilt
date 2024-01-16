package org.jilt.test.data.nullable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import javax.annotation.Nullable;

public final class FullName {
    public final String firstName, middleName, lastName;

    @Builder(style = BuilderStyle.TYPE_SAFE)
    public FullName(String firstName, @Nullable String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
