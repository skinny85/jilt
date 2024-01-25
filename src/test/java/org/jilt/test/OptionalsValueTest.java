package org.jilt.test;

import org.jilt.test.data.optionals.OptionalsRawValue;
import org.jilt.test.data.optionals.OptionalsRawValueBuilder;
import org.jilt.test.data.optionals.OptionalsValue;
import org.jilt.test.data.optionals.OptionalsValueBuilder;
import org.jilt.test.data.optionals.OptionalsWildcardValue;
import org.jilt.test.data.optionals.OptionalsWildcardValueBuilder;
import org.jilt.test.data.optionals.OptionalsWithOrderValue;
import org.jilt.test.data.optionals.OptionalsWithOrderValueBuilder;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionalsValueTest {
    @Test
    public void optional_type_property_is_implicitly_optional() {
        OptionalsValue<String> value = OptionalsValueBuilder.<String>optionalsValue()
                .v(null)
                .optional(Optional.of("abc"))
                .build();

        assertThat(value.optional).contains("abc");
        assertThat(value.v).isNull();
    }

    @Test
    public void optional_property_has_unwrapped_setter() {
        OptionalsValue<Integer> value = OptionalsValueBuilder.<Integer>optionalsValue()
                .v(null)
                .optional(33)
                .build();

        assertThat(value.optional).contains(33);
        assertThat(value.v).isNull();
    }

    @Test
    public void raw_optional_uses_java_lang_object_in_unwrapped_setter() {
        Object someObject = new Object();
        OptionalsRawValue value = OptionalsRawValueBuilder.optionalsRawValue()
                .rawOptional(someObject)
                .build();

        //noinspection unchecked
        assertThat(value.rawOptional).contains(someObject);
    }

    @Test
    public void wildcard_optional_uses_java_lang_object_in_unwrapped_setter() {
        Object someObject = new Object();
        OptionalsWildcardValue value = OptionalsWildcardValueBuilder.optionalsWildcardValue()
                .wildcardOptional(someObject)
                .build();

        assertThat(value.wildcardOptional).isEqualTo(Optional.of(someObject));
    }

    @Test
    public void null_can_be_passed_to_optional_unwrapped_setter() {
        List<String> nullList = null;
        OptionalsWithOrderValue<CharSequence> value = OptionalsWithOrderValueBuilder.<CharSequence>optionalsWithOrderValue()
                .optional(nullList)
                .v(null)
                .build();

        assertThat(value.optional).isEmpty();
    }
}
