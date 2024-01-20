package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class Generic3TypeParams<A, B, C> {
    @Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
    public static <B, C> Generic3TypeParams<?, B, C> make(Character a, @Opt B b, C c) {
        return new Generic3TypeParams<Character, B, C>(a, b, c);
    }

    public final A a;
    public final B b;
    public final C c;

    private Generic3TypeParams(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
