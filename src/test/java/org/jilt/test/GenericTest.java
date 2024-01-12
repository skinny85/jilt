package org.jilt.test;

import org.jilt.test.data.generic.Generic1TypeParam;
import org.jilt.test.data.generic.Generic1TypeParamBuilder;
import org.junit.Test;

public class GenericTest {
    @Test
    public void single_type_parameter_generic_works() {
        Generic1TypeParam value = new Generic1TypeParamBuilder()
                .build();
    }
}
