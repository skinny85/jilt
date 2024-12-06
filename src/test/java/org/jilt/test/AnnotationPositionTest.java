package org.jilt.test;

import org.jilt.test.data.annotation_position.TypeUseOnlyAnnotation;
import org.jilt.test.data.annotation_position.TypeUseOnlyBuilder;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationPositionTest {
    @Test
    public void test_type_use_annotation_on_correct_position() throws NoSuchMethodException {
        Method typeUseSetter = TypeUseOnlyBuilder.class.getMethod("typeUse", Map.Entry.class);

        assertThat(typeUseSetter.getAnnotatedParameterTypes()[0].isAnnotationPresent(TypeUseOnlyAnnotation.class)).isTrue();
    }
}
