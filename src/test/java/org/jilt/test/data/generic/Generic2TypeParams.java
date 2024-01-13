package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

public final class Generic2TypeParams<A1, A2> {
    public final A1 t1;
    public final A2 t2;

    @Builder(style = BuilderStyle.TYPE_SAFE)
    public Generic2TypeParams(A1 t1, A2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}
