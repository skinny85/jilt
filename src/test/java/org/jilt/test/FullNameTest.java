package org.jilt.test;

import org.jilt.test.data.nullable.FullName;
import org.jilt.test.data.nullable.FullNameBuilder;
import org.jilt.test.data.nullable.FullNameBuilders;
import org.jilt.test.data.nullable.FullNameJSpecify;
import org.jilt.test.data.nullable.FullNameJSpecifyBuilder;
import org.junit.Test;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.nullable.FullNameBuilder.fullName;

public class FullNameTest {
    @Test
    public void nullable_annotation_makes_attribute_optional() {
        FullName value = fullName()
                .firstName("First")
                // middleName is implicitly optional, because of @Nullable
                .lastName(null)
                .build();

        assertThat(value.firstName).isEqualTo("First");
        assertThat(value.middleName).isNull();
        assertThat(value.lastName).isNull();
    }

    @Test
    public void setter_in_builder_propagates_nullable_annotation() throws Exception {
        Method middleNameSetter = FullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_nullable_annotation() throws Exception {
        Method middleNameSetter = FullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void jspecify_nullable_annotation_makes_attribute_optional() {
        FullNameJSpecify value = FullNameJSpecifyBuilder.fullNameJSpecify()
                .firstName("First")
                // middleName is implicitly optional, because of @Nullable
                .lastName("")
                .build();

        assertThat(value.firstName).isEqualTo("First");
        assertThat(value.middleName).isNull();
        assertThat(value.lastName).isEmpty();
    }
}
