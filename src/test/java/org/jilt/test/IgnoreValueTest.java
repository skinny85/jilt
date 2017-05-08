package org.jilt.test;

import org.jilt.test.data.ignore.IgnoreValue;
import org.jilt.test.data.ignore.IgnoreValueBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IgnoreValueTest {
    @Test
    public void test_ignored_value_builder() throws Exception {
        IgnoreValue value = IgnoreValueBuilder.ignoreValue()
                .str("str")
//                .chr('a') // this has to be commented out (ignored field)
                .lng(23)
                .build();

        assertThat(value.str).isEqualTo("str");
        assertThat(value.chr).isEqualTo('\n');
        assertThat(value.lng).isEqualTo(23);
    }
}
