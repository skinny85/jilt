package org.jilt;

import org.junit.Test;

public class MainTest {
    @Test
    public void test_builder_class_generation() throws Exception {
        new TestClassBuilder()
                .name("some name");
    }
}
