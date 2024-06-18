package org.jilt.test;

import org.jilt.test.data.annotations.RepeatableAnnotations;
import org.jilt.test.data.annotations.RepeatableAnnotationsNameBuilder;
import org.jilt.test.data.annotations.RepeatableAnnotationsNameBuilders;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class RepeatableAnnotationsTest {
    @Test
    public void setter_in_builder_repeats_annotations() throws Exception {
        Method firstNameSetter = RepeatableAnnotationsNameBuilder.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);

        Method lastNameSetter = RepeatableAnnotationsNameBuilder.class.getMethod("lastName", String.class);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }

    @Test
    public void setter_in_type_safe_interface_repeats_annotations() throws Exception {
        Method firstNameSetter = RepeatableAnnotationsNameBuilders.FirstName.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);

        Method lastNameSetter = RepeatableAnnotationsNameBuilders.LastName.class.getMethod("lastName", String.class);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }
}
