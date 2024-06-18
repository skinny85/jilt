package org.jilt.test;

import org.jilt.test.data.annotations.NonRepeatableAnnotation;
import org.jilt.test.data.annotations.NonRepeatableAnnotationsNameBuilder;
import org.jilt.test.data.annotations.NonRepeatableAnnotationsNameBuilders;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class NonRepeatableAnnotationsTest {
    @Test
    public void setter_in_builder_does_not_repeat_annotations() throws Exception {
        Method nameSetter = NonRepeatableAnnotationsNameBuilder.class.getMethod("name", String.class);
        assertThat(nameSetter.getParameters()[0].getDeclaredAnnotation(NonRepeatableAnnotation.class)).isNotNull();
    }

    @Test
    public void setter_in_type_safe_interface_does_not_repeat_annotations() throws Exception {
        Method nameSetter = NonRepeatableAnnotationsNameBuilders.Name.class.getMethod("name", String.class);
        assertThat(nameSetter.getParameters()[0].getDeclaredAnnotation(NonRepeatableAnnotation.class)).isNotNull();
    }
}
