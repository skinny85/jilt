package org.jilt.test;

import org.jilt.test.data.defaultvalue.DefaultValueWithLombok;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.defaultvalue.DefaultValueWithLombokBuilder.defaultValueWithLombok;

public class LombokDefaultTest {
    @Test
    public void set_attribute_to_default_value_when_is_not_specified_in_builder_with_lombok_builder_default_value(){
        DefaultValueWithLombok value = defaultValueWithLombok()
                .charNoDefaultWithInit('x')
                .nr(11)
                .build();

        assertThat(value.getNr()).isEqualTo(11);
        assertThat(value.getStrNrPlus1k()).isEqualTo("1001");
        assertThat(value.getCharNoDefaultWithInit()).isEqualTo('x');
        assertThat(value.isBoolDefaultNoInit()).isFalse();
        assertThat(value.getStrings()).containsExactly("s");
        assertThat(value.getOptAttr()).isNull();
    }
}
