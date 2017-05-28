package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Builder {
    BuilderStyle style() default BuilderStyle.CLASSIC;

    String className() default "";

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Ignore {
    }
}
