package org.jilt.test;

import org.jilt.test.data.defaultvalue.DefaultValueWithLombok;
import org.jilt.test.data.defaultvalue.DefaultValueWithLombokBuilder;
import org.jilt.test.data.defaultvalue.DefaultValueWithOpt;
import org.jilt.test.data.defaultvalue.DefaultValueWithOptBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultTest {

    @Test
    public void set_attribute_to_default_value_when_is_not_specified_in_builder_with_lombok_builder_default_value(){
        DefaultValueWithLombok value = DefaultValueWithLombokBuilder
                .defaultValueWithLombok()
                .attr4(12)
                .attr5(13)
                .build();


        assertThat(value.attr1).isEqualTo(1);
        assertThat(value.attr2).isEqualTo("attr2");
        assertThat(value.attr3).isTrue();
        assertThat(value.attr4).isEqualTo(12);
        assertThat(value.attr5).isEqualTo(13);
        assertThat(value.attrs).isEqualTo(Collections.emptySet());
        assertThat(value.attrsWithNoDefault).isNull();
    }

    @Test
    public void set_attribute_to_default_value_when_is_not_specified_in_builder_with_opt(){
        DefaultValueWithOpt value = DefaultValueWithOptBuilder
                .defaultValueWithOpt()
                .attr4(12)
                .attr5(13)
                .build();


        assertThat(value.attr1).isEqualTo(1);
        assertThat(value.attr2).isEqualTo("attr2");
        assertThat(value.attr3).isTrue();
        assertThat(value.attr4).isEqualTo(12);
        assertThat(value.attr5).isEqualTo(13);
        assertThat(value.attrs).isEqualTo(Collections.emptySet());
        assertThat(value.attrsWithNoDefault).isNull();
    }

}
