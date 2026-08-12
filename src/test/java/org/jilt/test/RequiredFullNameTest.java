package org.jilt.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.jilt.test.data.nullable.RequiredFullName;
import org.jilt.test.data.nullable.RequiredFullNameBuilder;
import org.jilt.test.data.nullable.RequiredFullNameBuilders;
import org.junit.Test;

public class RequiredFullNameTest {
    @Test
    public void nullable_annotation_does_not_make_attribute_optional() {
        // NOTE: this would not compile if the middleName was considered optional in a STAGED builder.
        RequiredFullName fullName = RequiredFullNameBuilder.requiredFullName()
                .firstName("First")
                .middleName("Middle")
                .lastName("Last")
                .build();
        assertThat(fullName).isNotNull();
    }

    @Test
    public void setter_in_builder_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = RequiredFullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = RequiredFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }
}
