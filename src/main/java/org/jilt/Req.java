package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a field or constructor/static method parameter with this annotation
 * means the Builder property generated from that field or parameter will be required (so not optional).
 * <p>
 * Consumers of that generated Builder are required to provide values for required properties
 * before creating an instance of the target class.
 * <p>
 * By default, fields or parameters are considered required, so this annotation is not necessary.
 * However, a property will be considered optional if the field or parameter it was
 * generated from is annotated with a {@code @Nullable} annotation.
 * By also annotating such fields or parameters with @{@link Req} the property will be considered required again.
 *
 * @see Builder
 * @see Opt
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Req {
}
