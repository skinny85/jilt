package org.jilt.test.data.ignore;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class IgnoreValue {
    public final String str;

    @Builder.Ignore
    public final char chr = '\n';

    public final long lng;

    public IgnoreValue(String str, long lng) {
        this.str = str;
        this.lng = lng;
    }
}
