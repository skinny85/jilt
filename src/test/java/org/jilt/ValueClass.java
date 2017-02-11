package org.jilt;

@Builder(variant = BuilderVariant.TYPE_SAFE)
public class ValueClass {
    private static String staticName;

    private final String name;
    private final int age;
    private final String nick;

    public ValueClass(String name, int age, String nick) {
        this.name = name;
        this.age = age;
        this.nick = nick;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getNick() {
        return nick;
    }
}
