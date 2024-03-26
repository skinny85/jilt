package consumer.jilt;

import com.jilt.MyBuilder;

@MyBuilder
public final class ValueClass {
    public final String name;
    public final int age;
    public final char middleInitial;

    public ValueClass(String name, int age, char middleInitial) {
        this.name = name;
        this.age = age;
        this.middleInitial = middleInitial;
    }
}
