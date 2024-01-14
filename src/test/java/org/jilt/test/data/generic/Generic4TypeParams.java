package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class Generic4TypeParams<A, B, C, D> {
    @Builder(style = BuilderStyle.TYPE_SAFE)
    public static <A, C> Generic4TypeParams<A, String, C, Long> make(A a, @Opt String str, C c, @Opt Long lng) {
        return new Generic4TypeParams<A, String, C, Long>(a, str, c, lng);
    }

    public final A a;
    public final B b;
    public final C c;
    public final D d;

    private Generic4TypeParams(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }
}
