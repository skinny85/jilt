package org.jilt.test;

import org.jilt.test.data.typesafe.optional.TypeSafeOptionalsValue;
import org.jilt.test.data.typesafe.optional.TypeSafeOptionalsValueBuilder;
import org.jilt.test.data.typesafe.optional.TypeSafeOptionalsValueBuilders;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSafeOptionalsTest {
    @Test
    public void test_type_safe_builder_with_optional_values() throws Exception {
        TypeSafeOptionalsValueBuilders.Req1 firstInterface = TypeSafeOptionalsValueBuilder.typeSafeOptionalsValue();
        TypeSafeOptionalsValueBuilders.LastBuild lastInterface = firstInterface
                .req1(3.14)
                .req2("req2")
                .opt4(2.71F)
                .opt2(true)
                .opt3('c');
        TypeSafeOptionalsValue optionalsValue = lastInterface.build();

        assertThat(optionalsValue.getOpt1()).isNull();
        assertThat(optionalsValue.getOpt2()).isTrue();
        assertThat(optionalsValue.getOpt3()).isEqualTo('c');
        assertThat(optionalsValue.getReq1()).isEqualTo(3.14);
        assertThat(optionalsValue.getReq2()).isEqualTo("req2");
        assertThat(optionalsValue.getOpt4()).isEqualTo(2.71F);
        assertThat(optionalsValue.getOpt5()).isNull();
    }
}
