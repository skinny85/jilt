package org.jilt.test;

import org.jilt.test.data.defaultvalue.DefaultValueWithLombok;
import org.jilt.test.data.defaultvalue.DefaultValueWithLombokBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class LombokDefaultTest {
    @Test
    public void set_attribute_to_default_value_when_is_not_specified_in_builder_with_lombok_builder_default_value(){
        DefaultValueWithLombok value = DefaultValueWithLombokBuilder
                .defaultValueWithLombok()
                .nr(12)
                .strNrPlus1k("s")
                .charNoDefaultWithInit('x')
                .boolDefaultNoInit(true)
                .strings(Collections.singleton("S"))
                .build();

        assertThat(value.nr).isEqualTo(12);
        assertThat(value.strNrPlus1k).isEqualTo("s");
        assertThat(value.charNoDefaultWithInit).isEqualTo('x');
        assertThat(value.boolDefaultNoInit).isTrue();
        assertThat(value.strings).containsExactly("S");
        assertThat(value.optAttr).isNull();
    }
}
