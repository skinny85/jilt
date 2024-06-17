package org.jilt.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import javax.annotation.CheckForNull;
import org.jilt.test.data.non_repeatable.NonRepeatableAnnotation;
import org.jilt.test.data.non_repeatable.NonRepeatableAnnotationsNameBuilder;
import org.jilt.test.data.non_repeatable.NonRepeatableAnnotationsNameBuilders;
import org.junit.Test;

public class NonRepeatableAnnotationTest {
  @Test
  public void setter_in_builder_does_not_repeat_annotations() throws Exception {
    Method firstNameSetter = NonRepeatableAnnotationsNameBuilder.class.getMethod("firstName", String.class);
    assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(NonRepeatableAnnotation.class)).isNotNull();
  }

  @Test
  public void setter_in_type_safe_interface_does_not_repeat_annotations() throws Exception {
    Method firstNameSetter = NonRepeatableAnnotationsNameBuilders.FirstName.class.getMethod("firstName", String.class);
    assertThat(firstNameSetter.getParameters()[0].getDeclaredAnnotation(NonRepeatableAnnotation.class)).isNotNull();
  }
}
