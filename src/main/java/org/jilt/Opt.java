package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marking a field or constructor/static method parameter with this annotation
 * means the Builder property extracted from that field or parameter will be optional.
 * <p>
 * Builder clients are not required to provide values for optional properties
 * before building an instance of the target class. If not provided, the generated Builder
 * will pass default 0-values in place of the skipped optional properties
 * (<code>0</code> for <code>int</code>s, <code>null</code> for reference types etc.)
 * when constructing an instance of the target class.
 * <p>
 * How exactly does the API for skipping optional properties look like for the clients depends on the
 * {@link Builder#style} of the generated Builder - see the {@link BuilderStyle} enum values
 * documentation for details.
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
