package org.jilt.test.data.classic;

import org.jilt.Builder;

import java.util.Collections;
import java.util.List;

@Builder
public class ClassicValue {
    private static String staticName;

    private final String name;
    private final int age;
    private final String nick;
    private final List<String> securityAnswers;

    {
        int localVariable = -1;
    }

    public ClassicValue(String name, int age, String nick, List<String> securityAnswers) {
        this.name = name;
        this.age = age;
        this.nick = nick;
        this.securityAnswers = securityAnswers;
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

    public List<String> getSecurityAnswers() {
        return Collections.unmodifiableList(securityAnswers);
    }
}
