package org.jilt;

@Builder
public class TestClass {
    private final String name;

    public TestClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
