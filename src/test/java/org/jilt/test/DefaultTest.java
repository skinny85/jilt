package org.jilt.test;

import org.jilt.test.data.defaultvalue.DefaultValue;
import org.jilt.test.data.defaultvalue.DefaultValueBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultTest {

    @Test
    public void set_attribute_to_default_value_when_is_not_specified_in_builder(){
        DefaultValue value = DefaultValueBuilder
                .defaultValue()
                .attr4(12)
                .build();


        assertThat(value.attr1).isEqualTo(1);
        assertThat(value.attr2).isEqualTo("attr2");
        assertThat(value.attr3).isTrue();
        assertThat(value.attr4).isEqualTo(12);
        assertThat(value.attrs).isEqualTo(Collections.emptySet());
    }

}
