package org.jilt.test;

import org.jilt.test.data.constructor.ConstructorValue;
import org.jilt.test.data.constructor.ConstructorValueBuilder;
import org.jilt.test.data.typesafe.TypeSafeValue;
import org.jilt.test.data.typesafe.TypeSafeValueBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConstructorTest {
    @Test
    public void test_builder_on_constructor() throws Exception {
        ConstructorValue value = ConstructorValueBuilder.constructorValue()
                .attr2("attr2_value")
                .attr4(4)
                .attr3(true)
                .build();

        assertThat(value.attr1).isEqualTo(123);
        assertThat(value.attr2).isEqualTo("attr2_value");
        assertThat(value.attr3).isEqualTo(true);
        assertThat(value.attr4).isEqualTo(4);
    }
}
