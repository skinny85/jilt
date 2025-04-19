package org.jilt.test.data.generic;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style= BuilderStyle.STAGED)
public final class GenericTypeEnumParams<E extends Enum<E>> {
    public final E value;

    public GenericTypeEnumParams(E value) {
        this.value = value;
    }
}
