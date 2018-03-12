package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Customize the generated interfaces for Type-Safe Builder styles.
 * <p>
 * This annotation can be used alongside {@link Builder}.
 * If {@link Builder#style} is {@link BuilderStyle#TYPE_SAFE} or
 * {@link BuilderStyle#TYPE_SAFE_UNGROUPED_OPTIONALS},
 * you can use this annotation to customize the interfaces
 * generated for each property that ensure the Builder is type-safe.
 * If {@link Builder#style} is not a type-safe Builder style
 * (i.e., if it is {@link BuilderStyle#CLASSIC}),
 * this annotation has no effect.
 *
 * @since 1.1
 * @see #outerName
 * @see #packageName
 * @see Builder#style
 * @see BuilderStyle
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface BuilderInterfaces {
    /**
     * Allows you to set the name of the outer interface.
     * All interfaces used for ensuring type-safety
     * (there will be one per each property of the target class)
     * will be generated as inner interfaces of this outer interface,
     * in order not to pollute the global namespace.
     * <p>
     * This is an optional attribute, the default name is <code>&lt;TargetClass&gt;Builders</code>
     * (so, if we're building a <code>Person</code> class, the name will be <code>PersonBuilders</code>).
     */
    String outerName() default "";

    /**
     * Allows you to set the Java package that the generated interfaces will reside in.
     * This is an optional attribute -
     * the default is for the interfaces to reside in the same package as the Builder class.
     *
     * @see Builder#packageName
     */
    String packageName() default "";
}
