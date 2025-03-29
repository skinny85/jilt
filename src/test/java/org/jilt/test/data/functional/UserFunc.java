package org.jilt.test.data.functional;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class UserFunc {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.FUNCTIONAL, toBuilder = "copy")
    private UserFunc(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
                ? firstName + " " + lastName
                : displayName;
    }

    private static class InnerBuilder extends UserFuncBuilder {
        @Override
        public UserFunc build() {
            return new UserFunc(this.email, this.username, this.firstName, this.lastName, this.displayName);
        }
    }

    public static UserFunc userFunc(UserFuncBuilders.Email email,
            UserFuncBuilders.FirstName firstName, UserFuncBuilders.LastName lastName,
            UserFuncBuilders.Optional... optionals) {
        return UserFuncBuilder.userFunc(new InnerBuilder(), email, firstName, lastName, optionals);
    }

    public UserFunc copy(UserFuncBuilders.Setter... setters) {
        return UserFuncBuilder.copy(new InnerBuilder(), this, setters);
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
