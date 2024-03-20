package org.jilt.test;

import org.jilt.test.data.nullable.JSpecifyFullName;
import org.jilt.test.data.nullable.JSpecifyFullNameBuilder;
import org.jilt.test.data.nullable.JSpecifyFullNameBuilders;
import org.jspecify.annotations.Nullable;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class JSpecifyTest {
    @Test
    public void jspecify_nullable_annotation_makes_attribute_optional() {
        JSpecifyFullName value = JSpecifyFullNameBuilder.jSpecifyFullName()
                .firstName("First")
                // middleName is implicitly optional, because of @Nullable
                .lastName("")
                .build();

        assertThat(value.firstName).isEqualTo("First");
        assertThat(value.middleName).isNull();
        assertThat(value.lastName).isEmpty();
    }

    @Test
    public void setter_in_builder_propagates_jspecify_nullable_annotation() throws Exception {
        Method middleNameSetter = JSpecifyFullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotatedType().getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_jspecify_nullable_annotation() throws Exception {
        Method middleNameSetter = JSpecifyFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotatedType().getAnnotation(Nullable.class)).isNotNull();
    }
}
