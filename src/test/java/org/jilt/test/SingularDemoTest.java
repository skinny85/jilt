package org.jilt.test;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SingularDemoTest {
    @Test
    public void testSingularAddMethods() {
        SingularDemoBuilder builder = SingularDemoBuilder.singularDemo();
        builder.addTag("foo");
        builder.addTag("bar");
        builder.addAllTags(Arrays.asList("baz", "qux"));
        SingularDemo demo = builder.build();
        List<String> tags = demo.getTags();
        assertEquals(Arrays.asList("foo", "bar", "baz", "qux"), tags);
        assertTrue(tags instanceof java.util.ArrayList);
    }

    @Test
    public void testClassicSetterIgnoredForSingular() {
        SingularDemoBuilder builder = SingularDemoBuilder.singularDemo();
        // builder.tags(Arrays.asList("should", "not", "exist")); // Should not compile
        builder.addTag("only");
        SingularDemo demo = builder.build();
        assertEquals(Arrays.asList("only"), demo.getTags());
    }
}
