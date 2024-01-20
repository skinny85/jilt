package org.jilt.test;

import org.jilt.test.data.typesafe_ungrouped.TypeSafeValue;
import org.jilt.test.data.typesafe_ungrouped.custom.TypeSafeValueCreator;
import org.jilt.test.data.typesafe_ungrouped.custom.customer.TypeSafeValueCreators;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeSafeUngroupedTest {
    @Test
    public void test_type_safe_ungrouped_optionals_builder() {
        TypeSafeValueCreators.Step_Name firstInterface = TypeSafeValueCreator.creator();
        TypeSafeValueCreators.Step_Build lastInterface = firstInterface
                .withName("some name")
                .withAge(23)
                .withNick("some nickname");
        TypeSafeValue value = lastInterface.create();

        assertThat(value.getName()).isEqualTo("some name");
        assertThat(value.getAge()).isEqualTo(23);
        assertThat(value.getNick()).isEqualTo("some nickname");
    }
}
