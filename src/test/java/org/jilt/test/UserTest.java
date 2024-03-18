package org.jilt.test;

import org.jilt.test.data.user.User;
import org.jilt.test.data.user.UserBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    @Test
    public void user_can_be_created_through_builder() {
        User user = UserBuilder.user()
                .email("email@example.com")
                .firstName("First")
                .lastName("Last")
                .build();

        assertThat(user.email).isEqualTo("email@example.com");
        assertThat(user.username).isEqualTo("email@example.com");
        assertThat(user.firstName).isEqualTo("First");
        assertThat(user.lastName).isEqualTo("Last");
        assertThat(user.displayName).isEqualTo("First Last");
    }

    @Test
    public void only_a_single_required_property_can_be_set_with_to_builder() {
        User original = new User("email@example.com", null, "First", "Last", null);
        User modified = UserBuilder.modifiedUser(original)
                .firstName("John")
                .build();

        assertThat(modified.email).isEqualTo(original.email);
        assertThat(modified.username).isEqualTo(original.username);
        assertThat(modified.firstName).isNotEqualTo(original.firstName);
        assertThat(modified.lastName).isEqualTo(original.lastName);
        assertThat(modified.displayName).isEqualTo(original.displayName);
    }
}
