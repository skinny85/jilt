package org.jilt.test.data.lombok;

import org.jilt.Builder;

@Builder(toBuilder = "toBuilder")
public final class LombokGetterValue {
    private final String str;

    public LombokGetterValue(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
