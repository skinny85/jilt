package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class Generic3TypeParams<A, B, C> {
    @Builder(style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS)
    public static <A, B, C> Generic3TypeParams<A, B, C> make(A a, @Opt B b, C c) {
        return new Generic3TypeParams<A, B, C>(a, b, c);
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
