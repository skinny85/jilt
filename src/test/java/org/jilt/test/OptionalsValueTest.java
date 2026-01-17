package org.jilt.test;

import org.jilt.test.data.optionals.NullableOptionalsWithOrderValueBuilder;
import org.jilt.test.data.optionals.OptionalGetterValue;
import org.jilt.test.data.optionals.OptionalGetterValueBuilder;
import org.jilt.test.data.optionals.OptionalsRawValue;
import org.jilt.test.data.optionals.OptionalsRawValueBuilder;
import org.jilt.test.data.optionals.OptionalsRecordGetterValue;
import org.jilt.test.data.optionals.OptionalsRecordGetterValueBuilder;
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
    public void optional_type_property_is_not_implicitly_optional() {
        OptionalsValue<String> value = OptionalsValueBuilder.<String>optionalsValue()
                .optional(Optional.of("abc"))
                .v(null)
                .build();

        assertThat(value.getOptional()).contains("abc");
        assertThat(value.v).isNull();
    }
    @Test
    public void Optional_type_property_can_be_null() {
        OptionalsValue<String> value1 = OptionalsValueBuilder.<String>optionalsValue()
                .optional(null)
                .v(null)
                .build();

        OptionalsValue<String> value2 = OptionalsValueBuilder.toBuilder(value1)
                .build();

        assertThat(value2.getOptional()).isNull();
        assertThat(value2.v).isNull();
    }

    @Test
    public void raw_optional_uses_java_lang_object_in_setter() {
        Object someObject = new Object();
        OptionalsRawValue value = OptionalsRawValueBuilder.optionalsRawValue()
                .rawOptional(Optional.of(someObject))
                .build();

        //noinspection unchecked
        assertThat(value.rawOptional).contains(someObject);
    }

    @Test
    public void wildcard_optional_uses_java_lang_object_in_setter() {
        Object someObject = new Object();
        OptionalsWildcardValue value = OptionalsWildcardValueBuilder.optionalsWildcardValue()
                .wildcardOptional(Optional.of(someObject))
                .build();

        assertThat(value.wildcardOptional).isEqualTo(Optional.of(someObject));
    }

    @Test
    public void null_can_be_passed_to_optional_setter() {
        Optional<List<CharSequence>> nullList = null;
        OptionalsWithOrderValue<String> value = OptionalsWithOrderValueBuilder.<String>optionalsWithOrderValue()
                .optional(nullList)
                .v(null)
                .build();

        assertThat(value.optional).isNull();
    }

    @Test
    public void null_can_be_passed_to_unwrapped_optional_and_sets_empty() {
        OptionalsWithOrderValue<String> value = NullableOptionalsWithOrderValueBuilder.<String>optionalsWithOrderValue()
                .optional(null)
                .build();

        assertThat(value.optional).isEmpty();
        assertThat(value.v).isNull();
    }

    @Test
    public void getter_returning_Optional_works_with_toBuilder() {
        OptionalGetterValue value1 = OptionalGetterValueBuilder.optionalGetterValue()
                .withStrValue(null)
                .build();

        OptionalGetterValue value2 = OptionalGetterValueBuilder.toBuilder(value1)
                .build();

        assertThat(value2.getStrValue()).isEmpty();
    }

    @Test
    public void record_style_getter_returning_Optional_works_with_toBuilder() {
        OptionalsRecordGetterValue value1 = OptionalsRecordGetterValueBuilder.optionalsRecordGetterValue()
                .object(null)
                .optionalString(Optional.empty())
                .build();

        OptionalsRecordGetterValue value2 = OptionalsRecordGetterValueBuilder.toBuilder(value1)
                .build();

        assertThat(value2.object()).isEmpty();
        assertThat(value2.optionalString()).isEmpty();
    }
}
