package org.jilt.test.data.tobuilder;

import org.jilt.Builder;

import java.util.ArrayList;
import java.util.List;

public class ToBuilderValue {
    private final int getterAttr;
    private final List<String> methodAttr;
    final char fieldAttr;

    @Builder(toBuilder = "toBuilder")
    public ToBuilderValue(int getterAttr, List<String> methodAttr, char fieldAttr) {
        this.getterAttr = getterAttr;
        this.methodAttr = new ArrayList<String>(methodAttr);
        this.fieldAttr = fieldAttr;
    }

    public int getGetterAttr() {
        return getterAttr;
    }

    public List<String> methodAttr() {
        return methodAttr;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToBuilderValue)) {
            return false;
        }
        ToBuilderValue that = (ToBuilderValue) object;
        return this.getterAttr == that.getterAttr &&
                this.methodAttr.equals(that.methodAttr) &&
                this.fieldAttr == that.fieldAttr;
    }

    @Override
    public int hashCode() {
        return this.getterAttr + 17 * this.methodAttr.hashCode() + 31 * this.fieldAttr;
    }
}
