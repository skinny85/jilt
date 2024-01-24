package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a field or constructor/static method parameter with this annotation
 * means the Builder property generated from that field or parameter will be optional.
 * <p>
 * Consumers of that generated Builder are not required to provide values for optional properties
 * before creating an instance of the target class.
 * If not provided, the generated Builder will pass the default zero-value
 * (<code>0</code> for <code>int</code>s, <code>null</code> for reference types etc.)
 * for the given optional property when constructing an instance of the target class.
 * <p>
 * In addition to using this annotation,
 * a property will also be considered optional if:
 * <ul>
 *     <li>
 *         The field or parameter it represents is annotated with a {@code @Nullable} annotation.
 *         All types of {@code Nullable} annotations are supported,
 *         including {@code javax.annotation.Nullable}, {@code org.jetbrains.annotations.Nullable},
 *         and others.
 *     </li>
 *     <li>
 *         The field or parameter is of type {@code java.util.Optional}.
 *     </li>
 * </ul>
 * How exactly does the API for skipping optional properties look like for the consumers of the generated Builder class
 * depends on the choice of the {@link Builder#style} attribute used -
 * see the {@link BuilderStyle} enum values documentation for details.
 *
 * @since 1.0
 * @see Builder
 * @see Builder#style
 * @see BuilderStyle
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface Opt {
}
