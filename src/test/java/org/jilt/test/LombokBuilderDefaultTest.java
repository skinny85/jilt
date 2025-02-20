package org.jilt.test;

import org.jilt.test.data.lombok.LombokBuilderDefault;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.lombok.LombokBuilderDefaultBuilder.lombokBuilderDefault;

public class LombokBuilderDefaultTest {
    @Test
    public void Builder_Default_sets_property_value(){
        LombokBuilderDefault value = lombokBuilderDefault()
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
