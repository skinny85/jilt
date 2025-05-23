package org.jilt.test.data.typesafe.ungrouped;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;

@Builder(
        style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS,
        className = "*Creator",
        packageName = "org.jilt.test.data.typesafe.ungrouped.custom",
        setterPrefix = "with",
        factoryMethod = "creator",
        buildMethod = "create"
)
@BuilderInterfaces(
        packageName = "org.jilt.test.data.typesafe.ungrouped.custom.customer",
        innerNames = "Step_*"
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
