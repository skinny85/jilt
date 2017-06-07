package org.jilt.test.data.typesafe_ungrouped;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(
        style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS,
        className = "TypeSafeValueCreator",
        packageName = "org.jilt.test.data.typesafe_ungrouped.custom"
)
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
