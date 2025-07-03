package org.jilt.test.data.tobuilder;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = "toBuilder")
public class ToBuilderLombokGetter {
    private final String name;
    @Getter(AccessLevel.PACKAGE)
    private final int value;
    private final boolean active;
    private final Boolean enabled;

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
