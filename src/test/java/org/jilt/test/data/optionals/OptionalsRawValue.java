package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Optional;

public final class OptionalsRawValue {
    @SuppressWarnings("rawtypes")
    public final Optional rawOptional;

    @Builder(style = BuilderStyle.STAGED)
    public OptionalsRawValue(@SuppressWarnings("rawtypes") Optional rawOptional) {
        this.rawOptional = rawOptional;
    }
}
