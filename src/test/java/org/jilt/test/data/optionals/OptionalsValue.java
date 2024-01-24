package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Optional;

@Builder(style = BuilderStyle.STAGED)
public final class OptionalsValue<T> {
    public final Optional<T> optional;
    public final Void v;

    public OptionalsValue(Optional<T> optional, Void v) {
        this.optional = optional;
        this.v = v;
    }
}
