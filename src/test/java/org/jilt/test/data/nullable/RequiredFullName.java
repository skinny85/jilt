package org.jilt.test.data.nullable;

import javax.annotation.Nullable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Req;

public final class RequiredFullName {
    public final String firstName, middleName, lastName;

    @Builder(style = BuilderStyle.STAGED)
    public RequiredFullName(String firstName, @Nullable @Req String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
}
