package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.List;
import java.util.Optional;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public final class OptionalsWithOrderValue<T> {
    public final Optional<? extends List<? super T>> optional;
    public final Void v;

    public OptionalsWithOrderValue(Optional<? extends List<? super T>> optional, Void v) {
        this.optional = optional;
        this.v = v;
    }
}
