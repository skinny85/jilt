package org.jilt.test.data.tobuilder;

import org.jilt.Builder;

import java.util.ArrayList;
import java.util.List;

public class ToBuilderValue {
    private final int getterAttr;
    private final List<String> methodAttr;
    public final char fieldAttr;
    private final Boolean booleanGetter;

    @Builder(toBuilder = "toBuilder")
    public ToBuilderValue(int getterAttr, List<String> methodAttr, char fieldAttr, Boolean booleanGetter) {
        this.getterAttr = getterAttr;
        this.methodAttr = new ArrayList<String>(methodAttr);
        this.fieldAttr = fieldAttr;
        this.booleanGetter = booleanGetter;
    }

    public int getGetterAttr() {
        return getterAttr;
    }

    public List<String> methodAttr() {
        return methodAttr;
    }

    // Lombok generates Boolean getters with "get", not "is"
    public Boolean getBooleanGetter() {
        return booleanGetter;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ToBuilderValue)) {
            return false;
        }
        ToBuilderValue that = (ToBuilderValue) object;
        return this.getterAttr == that.getterAttr &&
                this.methodAttr.equals(that.methodAttr) &&
                this.fieldAttr == that.fieldAttr &&
                this.booleanGetter == that.booleanGetter;
    }

    @Override
    public int hashCode() {
        return this.getterAttr + 17 * this.methodAttr.hashCode() + 31 * this.fieldAttr +
                37 * this.booleanGetter.hashCode();
    }
}
