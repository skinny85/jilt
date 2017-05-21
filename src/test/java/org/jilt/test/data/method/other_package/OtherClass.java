package org.jilt.test.data.method.other_package;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.test.data.method.StaticFactoryValue;

public class OtherClass {
    @Builder(style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS)
    public static StaticFactoryValue create(int arg2, boolean arg4, String arg1, String arg3) {
        return new StaticFactoryValue(arg1, arg2, arg3, arg4);
    }
}
