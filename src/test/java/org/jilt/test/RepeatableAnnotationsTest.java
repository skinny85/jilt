package org.jilt.test;

import org.jilt.test.data.repeatable.RepeatableAnnotationNameBuilder;
import org.jilt.test.data.repeatable.RepeatableAnnotationNameBuilders;
import org.jilt.test.data.repeatable.RepeatableAnnotations;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class RepeatableAnnotationsTest {
    @Test
    public void setter_in_builder_propagates_repeatable_annotations() throws Exception {
        Method firstNameSetter = RepeatableAnnotationNameBuilder.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);

        Method middleNameSetter = RepeatableAnnotationNameBuilder.class.getMethod("middleName", String.class);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }

    @Test
    public void setter_in_type_safe_interface_propagates_repeatable_annotations() throws Exception {
        Method firstNameSetter = RepeatableAnnotationNameBuilders.FirstName.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);

        Method middleNameSetter = RepeatableAnnotationNameBuilders.MiddleName.class.getMethod("middleName", String.class);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }
}
