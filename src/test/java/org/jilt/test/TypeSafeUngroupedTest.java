package org.jilt.test;

import org.jilt.test.data.typesafe_ungrouped.TypeSafeValue;
import org.jilt.test.data.typesafe_ungrouped.TypeSafeValueCreator;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSafeUngroupedTest {
    @Test
    public void test_type_safe_builder() throws Exception {
        TypeSafeValue value = TypeSafeValueCreator.typeSafeValue()
                .name("some name")
                .age(23)
                .nick("some nickname")
                .build();

        assertThat(value.getName()).isEqualTo("some name");
        assertThat(value.getAge()).isEqualTo(23);
        assertThat(value.getNick()).isEqualTo("some nickname");
    }
}
