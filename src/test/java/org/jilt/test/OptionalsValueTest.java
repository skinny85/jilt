package org.jilt.test;

import org.jilt.test.data.optionals.OptionalsValue;
import org.jilt.test.data.optionals.OptionalsValueBuilder;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalsValueTest {
    @Test
    public void optional_property_is_always_optional() {
        OptionalsValue<String, Integer> value = OptionalsValueBuilder.<String, Integer>optionalsValue()
                .t2(33)
                .optional(Optional.of("abc"))
                .build();

        assertThat(value.optional).contains("abc");
        assertThat(value.t2).isEqualTo(33);
    }
}
