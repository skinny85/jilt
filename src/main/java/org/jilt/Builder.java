package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Builder {
    BuilderVariant variant() default BuilderVariant.CLASSIC;

    String[] optionalProperties() default {};
}
