package org.jilt.test;

import org.jilt.test.data.private_constructor.UserPriv;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PrivateConstructorTest {
    @Test
    public void class_with_private_constructor_can_be_built_through_builder() {
        UserPriv user = UserPriv.builder()
                .email("user@example.com")
                .firstName("First")
                .lastName(null)
                .build();

        assertThat(user.email).isEqualTo("user@example.com");
        assertThat(user.username).isEqualTo("user@example.com");
        assertThat(user.firstName).isEqualTo("First");
        assertThat(user.lastName).isNull();
        assertThat(user.displayName).isEqualTo("First null");
    }
}
