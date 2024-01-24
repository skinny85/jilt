package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.List;
import java.util.Optional;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public final class OptionalsWithOrderValue<T> {
    public final Optional<List<? extends T>> optional;
    public final Void v;

    public OptionalsWithOrderValue(Optional<List<? extends T>> optional, Void v) {
        this.optional = optional;
        this.v = v;
    }
}
