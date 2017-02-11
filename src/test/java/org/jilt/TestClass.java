package org.jilt;

@Builder
public class TestClass {
    private static String staticName;

    private final String name;

    public TestClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
