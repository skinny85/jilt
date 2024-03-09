package org.jilt.test;

import org.jilt.test.data.meta.MetaConstructorValue;
import org.jilt.test.data.meta.MetaConstructorValueBuilder;
import org.jilt.test.data.meta.MetaConstructorValueBuilders.Meta;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaAnnotationsTest {
    @Test
    public void test_meta_builder_on_constructor() {
        Meta lastValue = MetaConstructorValueBuilder.builder()
                .withAttr2("attr2_value")
                .withAttr4(4)
                .withAttr3(true);
        MetaConstructorValue value = lastValue.build();

        assertThat(value.attr1).isEqualTo(123);
        assertThat(value.attr2).isEqualTo("attr2_value");
        assertThat(value.attr3).isTrue();
        assertThat(value.attr4).isEqualTo(4);
    }
}
