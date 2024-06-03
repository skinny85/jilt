package org.jilt.test;

import org.jilt.test.data.annotations.PropagatedAnnotationNameBuilder;
import org.jilt.test.data.annotations.PropagatedAnnotationNameBuilders;
import org.junit.Test;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PropagatedAnnotationsTest {
  @Test
  public void setter_in_builder_propagates_annotations() throws Exception {
    Method lastNameSetter = PropagatedAnnotationNameBuilder.class.getMethod("lastName", String.class);

    assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
    assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(CheckForNull.class)).isNotNull();
  }

  @Test
  public void setter_in_type_safe_interface_propagates_annotations() throws Exception {
    Method lastNameSetter = PropagatedAnnotationNameBuilders.LastName.class.getMethod("lastName", String.class);

    assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(Nonnull.class)).isNotNull();
    assertThat(lastNameSetter.getParameters()[0].getDeclaredAnnotation(CheckForNull.class)).isNotNull();
  }
}
