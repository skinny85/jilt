package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a collection field or parameter for builder singular methods.
 * Only supported for List types in initial release.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Singular {
    /**
     * Optional: specify singular name if plural is irregular.
     */
    String value() default "";
}
