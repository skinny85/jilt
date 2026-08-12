package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a field or constructor/static method parameter with this annotation
 * means the Builder property generated from that field or parameter will be required
 * (so, not optional).
 * <p>
 * Consumers of that generated Builder are required to provide values for required properties
 * before creating an instance of the target class
 * (required properties are only supported if the {@link Builder#style}
 * attribute of the {@code @Builder} annotation that defines a given Builder
 * has a value other than the default {@link BuilderStyle#CLASSIC},
 * since "classic" Builders treat all properties as optional).
 * <p>
 * <b>Note</b>: properties are considered required by default,
 * so this annotation is only necessary if a given property is annotated with a
 * {@code @Nullable} annotation,
 * but you still want it to be required
 * (without {@link Req}, Jilt treats properties annotated with any {@code @Nullable}
 * annotation automatically as optional,
 * even if they aren't annotated with {@link Opt}).
 *
 * @since 1.9.2
 * @see Builder
 * @see Opt
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Req {
}
