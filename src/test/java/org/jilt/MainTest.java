package org.jilt;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MainTest {
    @Test
    public void test_builder_class_generation() throws Exception {
        ValueClass value = ValueClassBuilder.valueClass()
                .name("some name")
                .age(23)
                .nick("some nickname")
                .build();

        assertThat(value.getName()).isEqualTo("some name");
        assertThat(value.getAge()).isEqualTo(23);
        assertThat(value.getNick()).isEqualTo("some nickname");
    }
}
