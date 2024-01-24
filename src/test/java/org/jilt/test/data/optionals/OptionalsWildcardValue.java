package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Optional;

public final class OptionalsWildcardValue {
    public final Optional<?> wildcardOptional;

    @Builder(style = BuilderStyle.STAGED)
    public OptionalsWildcardValue(Optional<?> wildcardOptional) {
        this.wildcardOptional = wildcardOptional;
    }
}
