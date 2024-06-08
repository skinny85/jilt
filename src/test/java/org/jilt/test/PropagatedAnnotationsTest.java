package org.jilt.test;

import org.jilt.test.data.annotations.NonParameterAnnotation;
import org.jilt.test.data.annotations.PropagatedAnnotationNameBuilder;
import org.jilt.test.data.annotations.PropagatedAnnotationNameBuilders;
import org.junit.Test;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PropagatedAnnotationsTest {
    @Test
    public void setter_in_builder_propagates_annotations() throws Exception {
        Method firstNameSetter = PropagatedAnnotationsFullNameBuilder.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(CheckForNull.class)).isNotNull();

        Method middleNameSetter = PropagatedAnnotationsFullNameBuilder.class.getMethod("middleName", String.class);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(Nullable.class)).isNotNull();

        Method lastNameSetter = PropagatedAnnotationsFullNameBuilder.class.getMethod("lastName", String.class);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(NonParameterAnnotation.class)).isNull();
    }

    @Test
    public void setter_in_type_safe_interface_propagates_annotations() throws Exception {
        Method firstNameSetter = PropagatedAnnotationsFullNameBuilders.FirstName.class.getMethod("firstName", String.class);
        assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(CheckForNull.class)).isNotNull();

        Method middleNameSetter = PropagatedAnnotationsFullNameBuilders.MiddleName.class.getMethod("middleName", String.class);
        assertThat(middleNameSetter.getParameters()[0].getDeclaredAnnotation(Nullable.class)).isNotNull();

        Method lastNameSetter = PropagatedAnnotationsFullNameBuilders.LastName.class.getMethod("lastName", String.class);
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
        assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(NonParameterAnnotation.class)).isNull();
    }
}
