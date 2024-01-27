package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public final class OptionalsWithOrderValue<T> {
    public final Optional<? extends List<? super T>> optional;
    public final Void v;

    public OptionalsWithOrderValue(Optional<? extends List<? super T>> optional, Void v) {
        this.optional = optional;
        this.v = v;
    }

    @Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER,
        className = "NullableOptionalsWithOrderValueBuilder")
    static <T> OptionalsWithOrderValue<T> unwrapped(@Nullable List<? super T> optional, @Opt Void v) {
        return new OptionalsWithOrderValue<>(Optional.ofNullable(optional), v);
    }
}
