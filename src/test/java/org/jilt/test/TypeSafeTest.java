package org.jilt.test;

import org.jilt.test.data.typesafe.TypeSafeValue;
import org.jilt.test.data.typesafe.TypeSafeValueBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSafeTest {
    @Test
    public void test_type_safe_builder() throws Exception {
        TypeSafeValue value = TypeSafeValueBuilder.typeSafeValue()
                .name("some name")
                .age(23)
                .nick("some nickname")
                .build();

        assertThat(value.getName()).isEqualTo("some name");
        assertThat(value.getAge()).isEqualTo(23);
        assertThat(value.getNick()).isEqualTo("some nickname");
    }
}
