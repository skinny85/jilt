package org.jilt.test.data.typesafe;

import org.jilt.Builder;
import org.jilt.BuilderVariant;

@Builder(variant = BuilderVariant.TYPE_SAFE)
public class TypeSafeValue {
    private final String name;
    private final int age;
    private final String nick;

    public TypeSafeValue(String name, int age, String nick) {
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
