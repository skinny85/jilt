package org.jilt.test.data.tobuilder;

import lombok.Data;
import org.jilt.Builder;

import java.util.Objects;

@Data
@Builder(toBuilder = "toBuilder", packageName = "org.jilt.test.data.tobuilder.lombok")
public class ToBuilderLombokData {
    public final String name;
    private final int value;
    final boolean active;
    protected final Boolean enabled;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToBuilderLombokData)) {
            return false;
        }
        ToBuilderLombokData that = (ToBuilderLombokData) object;
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
