package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Optional;

@Builder(style = BuilderStyle.STAGED)
public final class OptionalsValue<T1, T2> {
    public final Optional<T1> optional;
    public final T2 t2;

    public OptionalsValue(Optional<T1> optional, T2 t2) {
        this.optional = optional;
        this.t2 = t2;
    }
}
