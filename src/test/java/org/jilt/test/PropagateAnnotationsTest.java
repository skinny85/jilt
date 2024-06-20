package org.jilt.test;

import org.jilt.test.data.annotations.FieldOnlyAnnotation;
import org.jilt.test.data.annotations.FieldParamTypeUseAnnotation;
import org.jilt.test.data.annotations.PropagateAnnotationsFullNameBuilder;
import org.jilt.test.data.annotations.PropagateAnnotationsFullNameBuilders;
import org.jilt.test.data.annotations.RepeatableAnnotation;
import org.jilt.test.data.annotations.RepeatableAnnotations;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PropagateAnnotationsTest {
    @Test
    public void setter_in_builder_does_not_repeat_type_use_annotation() throws Exception {
        Method firstNameSetter = PropagateAnnotationsFullNameBuilder.class.getMethod("firstName", String.class);

        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(FieldParamTypeUseAnnotation.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_does_not_repeat_type_use_annotation() throws Exception {
        Method firstNameSetter = PropagateAnnotationsFullNameBuilders.FirstName.class.getMethod("firstName", String.class);

        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(FieldParamTypeUseAnnotation.class)).isNotNull();
    }

    @Test
    public void setter_in_builder_propagates_repeated_annotations() throws Exception {
        Method middleNameSetter = PropagateAnnotationsFullNameBuilder.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotationsByType(RepeatableAnnotation.class)).hasSize(2);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }

    @Test
    public void setter_in_type_safe_interface_propagates_repeated_annotations() throws Exception {
        Method middleNameSetter = PropagateAnnotationsFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);

        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotationsByType(RepeatableAnnotation.class)).hasSize(2);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
    }

    @Test
    public void setter_in_builder_propagates_repeated_containing_annotations() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilder.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotationsByType(RepeatableAnnotation.class)).hasSize(2);
    }

    @Test
    public void setter_in_type_safe_interface_propagates_repeated_containing_annotations() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilders.MiddleName.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(RepeatableAnnotations.class).value()).hasSize(2);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotationsByType(RepeatableAnnotation.class)).hasSize(2);
    }

    @Test
    public void setter_in_builder_propagates_annotations_without_target_from_field() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilder.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_annotations_without_target_from_field() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilders.LastName.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
    }

    @Test
    public void setter_in_builder_does_not_propagate_field_only_annotations() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilder.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(FieldOnlyAnnotation.class)).isNull();
    }

    @Test
    public void setter_in_type_safe_interface_does_not_propagate_field_only_annotations() throws Exception {
        Method lastNameSetter = PropagateAnnotationsFullNameBuilders.LastName.class.getMethod("lastName", String.class);

        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(FieldOnlyAnnotation.class)).isNull();
    }
}
