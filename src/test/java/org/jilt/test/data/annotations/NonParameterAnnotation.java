package org.jilt.test.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
        ElementType.ANNOTATION_TYPE, ElementType.PACKAGE, ElementType.TYPE_PARAMETER})
public @interface NonParameterAnnotation {
}
