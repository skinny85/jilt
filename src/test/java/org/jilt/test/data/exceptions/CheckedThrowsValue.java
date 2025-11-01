package org.jilt.test.data.exceptions;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.io.IOException;

public final class CheckedThrowsValue {
    @Builder(toBuilder = "toBuilder")
    public CheckedThrowsValue() throws IOException {
    }

    @Builder(className = "CheckedThrowsValueStagedBuilder", style = BuilderStyle.STAGED, toBuilder = "toBuilder")
    public CheckedThrowsValue(int intValue) throws IOException {
    }

    @Builder(className = "CheckedThrowsValueFunctionalBuilder", style = BuilderStyle.FUNCTIONAL, toBuilder = "toBuilder")
    public static CheckedThrowsValue make() throws IOException {
        return new CheckedThrowsValue();
    }

    public int getIntValue() {
        throw new RuntimeException("CheckedThrowsValue.getIntValue() should never be called");
    }
}
