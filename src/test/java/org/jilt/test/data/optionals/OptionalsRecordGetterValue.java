package org.jilt.test.data.optionals;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Optional;

public final class OptionalsRecordGetterValue {
    private final Object object;
    private final Optional<String> optionalString;

    @Builder(style = BuilderStyle.STAGED, toBuilder = "toBuilder")
    public OptionalsRecordGetterValue(Object object, Optional<String> optionalString) {
        this.object = object;
        this.optionalString = optionalString;
    }

    /** Test handling of a raw Optional. */
    @SuppressWarnings("rawtypes")
    public Optional object() {
        return Optional.ofNullable(this.object);
    }

    public Optional<String> optionalString() {
        return this.optionalString;
    }
}
