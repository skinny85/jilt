package org.jilt.test;

import org.jilt.test.data.tobuilder.ToBuilderValue;
import org.jilt.test.data.tobuilder.ToBuilderValueBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ToBuilderTest {
    @Test
    public void test_to_builder() {
        ToBuilderValue value = new ToBuilderValue(1, Collections.singletonList("A"), 'a');
        ToBuilderValue builder = ToBuilderValueBuilder.toBuilder(value)
                .build();

        assertThat(value).isEqualTo(builder);
    }
}
