package org.jilt.test.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
public @interface NonParameterAnnotation {
}
