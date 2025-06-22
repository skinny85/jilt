package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.FUNCTIONAL)
public class Generic1TypeParamF<T> {
    // it's important for this test that the field and the type parameter have the same name,
    // as that tests some edge cases where the type parameter in the Functional setter interfaces
    // can be shadowed by the generated interface name
    public final T t;

    public Generic1TypeParamF(T t) {
        this.t = t;
    }
}
