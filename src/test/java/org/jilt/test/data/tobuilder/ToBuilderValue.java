package org.jilt.test.data.tobuilder;

import org.jilt.Builder;

import java.util.List;

@Builder(toBuilder = true)
public class ToBuilderValue {
    private final int getterAttr;
    private final List<String> methodAttr;
    final char fieldAttr;

    public ToBuilderValue(int getterAttr, List<String> methodAttr, char fieldAttr) {
        this.getterAttr = getterAttr;
        this.methodAttr = methodAttr;
        this.fieldAttr = fieldAttr;
    }

    public int getGetterAttr() {
        return getterAttr;
    }

    public List<String> methodAttr() {
        return methodAttr;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ToBuilderValue)) {
            return false;
        }
        ToBuilderValue that = (ToBuilderValue) obj;
        return this.getterAttr == that.getterAttr &&
                this.methodAttr.equals(that.methodAttr) &&
                this.fieldAttr == that.fieldAttr;
    }

    @Override
    public int hashCode() {
        return this.getterAttr + 17 * this.methodAttr.hashCode() + 31 * this.fieldAttr;
    }
}
