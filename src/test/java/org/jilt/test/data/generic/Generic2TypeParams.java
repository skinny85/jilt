package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.io.Serializable;

public final class Generic2TypeParams<T1 extends Number & Serializable, T2> {
    public final T1 t1;
    public final T2 t2;

    @Builder(style = BuilderStyle.STAGED)
    public Generic2TypeParams(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }
}
