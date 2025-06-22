package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.FUNCTIONAL)
public class Generic1TypeParamF<T> {
    public final T t;

    public Generic1TypeParamF(T t) {
        this.t = t;
    }
}
