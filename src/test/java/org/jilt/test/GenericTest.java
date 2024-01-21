package org.jilt.test;

import org.jilt.test.data.generic.Generic1TypeParam;
import org.jilt.test.data.generic.Generic1TypeParamBuilder;
import org.jilt.test.data.generic.Generic2TypeParams;
import org.jilt.test.data.generic.Generic2TypeParamsBuilder;
import org.jilt.test.data.generic.Generic3TypeParams;
import org.jilt.test.data.generic.Generic3TypeParamsBuilder;
import org.jilt.test.data.generic.Generic4TypeParams;
import org.jilt.test.data.generic.Generic4TypeParamsBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GenericTest {
    @Test
    public void single_type_parameter_generic_class_with_classic_builder_works() {
        Generic1TypeParam<String> value = new Generic1TypeParamBuilder<String>()
                .t("a")
                .build();

        assertThat(value.t).isEqualTo("a");
    }

    @Test
    public void two_type_parameters_generic_class_with_staged_builder_works() {
        Generic2TypeParams<Integer, Boolean> value = Generic2TypeParamsBuilder.<Integer, Boolean>generic2TypeParams()
                .t1(33)
                .t2(true)
                .build();

        assertThat(value.t1).isEqualTo(33);
        assertThat(value.t2).isTrue();
    }

    @Test
    public void three_type_parameters_generic_class_with_staged_preserving_order_builder_works() {
        Generic3TypeParams<?, Double, String> value = Generic3TypeParamsBuilder.<Double, String>generic3TypeParams()
                .a('3')
                .b(3.14)
                .c("41")
                .build();

        assertThat(value.a).isEqualTo('3');
        assertThat(value.b).isEqualTo(3.14);
        assertThat(value.c).isEqualTo("41");
    }

    @Test
    public void four_type_parameters_generic_class_with_static_factory_method_and_subset_of_type_parameters_works() {
        Generic4TypeParams<Boolean, ?, Boolean, Long> value = Generic4TypeParamsBuilder.<Boolean, Boolean>generic4TypeParams()
                .a(true)
                .str("str")
                .c(null)
                .build();

        assertThat(value.a).isTrue();
        assertThat(value.b).isEqualTo("str");
        assertThat(value.c).isNull();
        assertThat(value.d).isNull();
    }
}
