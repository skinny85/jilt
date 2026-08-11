package org.jilt.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.jilt.test.data.nullable.NotNullableFullNameBuilder;
import org.jilt.test.data.nullable.NotNullableFullNameBuilders;
import org.junit.Test;

public class NotNullableFullNameTest {
    @Test
    public void nullable_annotation_does_not_make_attribute_optional() {
        assertThat(NotNullableFullNameBuilders.MiddleName.class.getMethods()).singleElement().extracting(Method::getName).isEqualTo("middleName");
    }

    @Test
    public void setter_in_builder_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = NotNullableFullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_nullable_annotation_from_constructor_parameter() throws Exception {
        Method middleNameSetter = NotNullableFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getAnnotation(Nullable.class)).isNotNull();
    }
}
