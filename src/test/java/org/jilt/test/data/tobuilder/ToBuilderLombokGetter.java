package org.jilt.test.data.tobuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = "toBuilder")
public class ToBuilderLombokGetter {
    public final String name;
    @Getter(AccessLevel.PACKAGE)
    private final int value;
    final boolean active;
    protected final Boolean enabled;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToBuilderLombokGetter)) {
            return false;
        }
        ToBuilderLombokGetter that = (ToBuilderLombokGetter) object;
        return Objects.equals(this.name, that.name) &&
                this.value == that.value &&
                this.active == that.active &&
                this.enabled == that.enabled;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode() + 17 * this.value + 31 * (this.active ? 1 : 0) + 
                37 * this.enabled.hashCode();
    }

}
