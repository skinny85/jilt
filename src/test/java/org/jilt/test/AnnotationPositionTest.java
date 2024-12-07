package org.jilt.test;

import org.jilt.test.data.annotation_position.TypeUseOnlyAnnotation;
import org.jilt.test.data.annotation_position.TypeUseOnlyBuilder;
import org.junit.Assume;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationPositionTest {
    @Test
    public void test_type_use_annotation_on_correct_position() throws NoSuchMethodException {
        int jreVersion = getJreVersion();

        // Java 8 has some bugs in Language Model API, see https://bugs.openjdk.org/browse/JDK-8031744
        // Therefore, Jilt cannot correctly detect TYPE_USE-only annotations.
        Assume.assumeTrue(jreVersion >= 9);

        Method typeUseSetter = TypeUseOnlyBuilder.class.getMethod("typeUse", Map.Entry.class);

        if (jreVersion < 13) {
            assertThat(
                    typeUseSetter
                            .getAnnotatedParameterTypes()[0]
                            // Before Java 13, the Reflect API
                            // cannot correctly handle the annotation on static inner class,
                            // it will push annotations to 'OwnerType'
                            // see https://bugs.openjdk.org/browse/JDK-8198526
                            .getAnnotatedOwnerType() // ! @since 9
                            .isAnnotationPresent(TypeUseOnlyAnnotation.class)
            ).isTrue();

        } else {
            assertThat(
                    typeUseSetter
                            .getAnnotatedParameterTypes()[0]
                            .isAnnotationPresent(TypeUseOnlyAnnotation.class)
            ).isTrue();
        }
    }

    private int getJreVersion() {
        String[] versionElements = System.getProperty("java.version").split("\\.");

        int discard = Integer.parseInt(versionElements[0]);

        int version;
        if (discard == 1) {
            version = Integer.parseInt(versionElements[1]);
        } else {
            version = discard;
        }

        return version;
    }
}
