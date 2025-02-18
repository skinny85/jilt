package org.jilt.test.data.private_constructor;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class UserPriv {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.STAGED, toBuilder = "toBuilder")
    private UserPriv(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
                ? firstName + " " + lastName
                : displayName;
    }

    private static class InnerBuilder extends UserPrivBuilder {
        @Override
        public UserPriv build() {
            return new UserPriv(email, username, firstName, lastName, displayName);
        }
    }

    public static UserPrivBuilders.Email builder() {
        return new InnerBuilder();
    }

    public UserPrivBuilder toBuilder() {
        return UserPrivBuilder.toBuilder(new InnerBuilder(), this);
    }
}
