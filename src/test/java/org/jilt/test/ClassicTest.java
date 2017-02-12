package org.jilt.test;

import org.jilt.test.data.classic.ClassicValue;
import org.jilt.test.data.classic.ClassicValueBuilder;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassicTest {
    @Test
    public void test_classic_builder() throws Exception {
        ClassicValue value = new ClassicValueBuilder()
                .nick("some nickname")
                .securityAnswers(asList("Irene", "Whiskers"))
                .name("some name")
                .nick("other nickname")
                .age(23)
                .build();

        assertThat(value.getName()).isEqualTo("some name");
        assertThat(value.getAge()).isEqualTo(23);
        assertThat(value.getNick()).isEqualTo("other nickname");
        assertThat(value.getSecurityAnswers()).containsExactly("Irene", "Whiskers");
    }
}
