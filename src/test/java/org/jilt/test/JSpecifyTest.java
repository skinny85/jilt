package org.jilt.test;

import org.jilt.test.data.nullable.JSpecifyFullName;
import org.jilt.test.data.nullable.JSpecifyFullNameBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JSpecifyTest {
    @Test
    public void jspecify_nullable_annotation_makes_attribute_optional() {
        JSpecifyFullName value = JSpecifyFullNameBuilder.jSpecifyFullName()
                .firstName("First")
                // middleName is implicitly optional, because of @Nullable
                .lastName("")
                .build();

        assertThat(value.firstName).isEqualTo("First");
        assertThat(value.middleName).isNull();
        assertThat(value.lastName).isEmpty();
    }
}
