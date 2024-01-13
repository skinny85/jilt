package org.jilt.test;

import org.jilt.test.data.generic.Generic1TypeParam;
import org.jilt.test.data.generic.Generic1TypeParamBuilder;
import org.jilt.test.data.generic.Generic2TypeParams;
import org.jilt.test.data.generic.Generic2TypeParamsBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericTest {
    @Test
    public void single_type_parameter_generic_works() {
        Generic1TypeParam<String> value = new Generic1TypeParamBuilder<String>()
                .t("a")
                .build();

        assertThat(value.t).isEqualTo("a");
    }

    @Test
    public void two_type_parameters_generic_works() {
        Generic2TypeParams<Integer, Boolean> value = Generic2TypeParamsBuilder.<Integer, Boolean>generic2TypeParams()
                .t1(33)
                .t2(true)
                .build();

        assertThat(value.t1).isEqualTo(33);
        assertThat(value.t2).isTrue();
    }
}
