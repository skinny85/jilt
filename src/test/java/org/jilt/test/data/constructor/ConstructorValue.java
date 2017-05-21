package org.jilt.test.data.constructor;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

public final class ConstructorValue {
    public final int attr1;
    public final String attr2;
    public final boolean attr3;
    public final int attr4;

    @Builder(style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS)
    public ConstructorValue(String attr2, int attr4, boolean attr3) {
        this.attr1 = 123;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
    }
}
