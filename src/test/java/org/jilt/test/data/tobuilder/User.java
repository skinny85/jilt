package org.jilt.test.data.tobuilder;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class User {
    public final String email, username, firstName, lastName, displayName;

    /**
     * Example JavaDoc of constructor of {@link User}.
     *
     * @param email example JavaDoc for email parameter
     * @param username example JavaDoc for username parameter
     * @param firstName example JavaDoc for firstName parameter
     * @param lastName example JavaDoc for lastName parameter
     * @param displayName example JavaDoc for displayName parameter
     */
    @Builder(style = BuilderStyle.STAGED, toBuilder = "modifiedUser")
    public User(String email, @Opt String username, String firstName,
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
