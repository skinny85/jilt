package org.jilt.test.data.optionals;

import org.jilt.Builder;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Builder(setterPrefix = "with", toBuilder = "toBuilder")
public final class OptionalGetterValue {
    @Nullable
    private final String strValue;

    public OptionalGetterValue(@Nullable String strValue) {
        this.strValue = strValue;
    }

    @Nonnull
    public Optional<String> getStrValue() {
        return Optional.ofNullable(this.strValue);
    }
}
