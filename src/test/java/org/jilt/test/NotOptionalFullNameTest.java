package org.jilt.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.jilt.test.data.nullable.NotOptionalFullName;
import org.jilt.test.data.nullable.NotOptionalFullNameBuilder;
import org.jilt.test.data.nullable.NotOptionalFullNameBuilders;
import org.junit.Test;

public class NotOptionalFullNameTest {
    @Test
    public void nullable_annotation_does_not_make_attribute_optional() {
        // NOTE: this would not compile if the middleName was considered optional in a STAGED builder.
        NotOptionalFullName fullName = NotOptionalFullNameBuilder.notOptionalFullName()
                .firstName("First")
                .middleName("Middle")
                .lastName("Last")
                .build();
        assertThat(fullName).isNotNull();
    }

    @Test
    public void setter_in_builder_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = NotOptionalFullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = NotOptionalFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }
}
