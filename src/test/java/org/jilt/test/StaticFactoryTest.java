package org.jilt.test;

import org.jilt.test.data.method.StaticFactoryValue;
import org.jilt.test.data.method.StaticFactoryValueBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StaticFactoryTest {
    @Test
    public void test_builder_on_factory_method() throws Exception {
        StaticFactoryValue value = StaticFactoryValueBuilder.staticFactoryValue()
                .arg2(2)
                .arg4(true)
                .arg1("attr1_val")
                .arg3("attr3_val")
                .build();

        assertThat(value.attr1).isEqualTo("attr1_val");
        assertThat(value.attr2).isEqualTo(2);
        assertThat(value.attr3).isEqualTo("attr3_val");
        assertThat(value.attr4).isTrue();
    }
}
