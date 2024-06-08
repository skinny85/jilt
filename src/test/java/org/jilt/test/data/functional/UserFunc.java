package org.jilt.test.data.functional;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class UserFunc {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.FUNCTIONAL, toBuilder = "toBuilder")
    public UserFunc(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
                ? firstName + " " + lastName
                : displayName;
    }
}
