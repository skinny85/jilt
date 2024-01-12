package org.jilt.test.data.generic;

import org.jilt.Builder;

@Builder
public class Generic1TypeParam<T> {
    public final T t;

    public Generic1TypeParam(T t) {
        this.t = t;
    }
}
