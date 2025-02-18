package org.jilt.test;

import org.jilt.test.data.annotations.type.TypeUseOnlyAnnotation;
import org.jilt.test.data.annotations.type.TypeUseExampleBuilder;
import org.jilt.test.data.annotations.type.TypeUseExampleBuilders;
import org.junit.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Due to various bugs in the Java Language Model API,
 * like https://bugs.openjdk.org/browse/JDK-8031744 for Java 8,
 * and https://bugs.openjdk.org/browse/JDK-8198526 before 13,
 * this test is only enabled for Java 17+.
 */
public class TypeUseAnnotationsTest {
    @Test
    public void builder_setter_has_type_use_annotation() throws NoSuchMethodException {
        Method entrySetter = TypeUseExampleBuilder.class.getMethod("entry", Map.Entry.class);
        AnnotatedType[] annotatedParameterTypes = entrySetter.getAnnotatedParameterTypes();
        assertThat(annotatedParameterTypes).hasSize(1);
        assertThat(annotatedParameterTypes[0].isAnnotationPresent(TypeUseOnlyAnnotation.class)).isTrue();
    }

    @Test
    public void staged_interface_setter_has_type_use_annotation() throws NoSuchMethodException {
        Method entrySetter = TypeUseExampleBuilders.Entry.class.getMethod("entry", Map.Entry.class);
        AnnotatedType[] annotatedParameterTypes = entrySetter.getAnnotatedParameterTypes();
        assertThat(annotatedParameterTypes).hasSize(1);
        assertThat(annotatedParameterTypes[0].isAnnotationPresent(TypeUseOnlyAnnotation.class)).isTrue();
    }
}
