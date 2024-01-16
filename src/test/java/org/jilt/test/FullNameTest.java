package org.jilt.test;

import org.jilt.test.data.nullable.FullName;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.nullable.FullNameBuilder.fullName;

public class FullNameTest {
    @Test
    public void nullable_attribute() throws Exception {
        FullName value = fullName()
                .firstName("First")
                .lastName(null)
                // middleName is implicitly optional, because of @Nullable
                .build();

        assertThat(value.firstName).isEqualTo("First");
        assertThat(value.middleName).isNull();
        assertThat(value.lastName).isNull();
    }
}
