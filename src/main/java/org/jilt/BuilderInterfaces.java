package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Customize the generated interfaces for Staged Builder styles.
 * <p>
 * This annotation can be used alongside {@link Builder}.
 * If {@link Builder#style} is {@link BuilderStyle#STAGED} or
 * {@link BuilderStyle#STAGED_PRESERVING_ORDER},
 * you can use this annotation to customize the interfaces
 * generated for each property that ensure the Builder is type-safe.
 * If {@link Builder#style} is not a Staged Builder style
 * (i.e., if it is {@link BuilderStyle#CLASSIC}),
 * this annotation has no effect.
 *
 * @since 1.1
 * @see #outerName
 * @see #packageName
 * @see #innerNames
 * @see #lastInnerName
 * @see Builder#style
 * @see BuilderStyle
 */
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface BuilderInterfaces {
    /**
     * Allows you to set the name of the outer interface.
     * All interfaces used for ensuring type-safety
     * (there will be one per each property of the target class)
     * will be generated as inner interfaces of this outer interface,
     * in order not to pollute the global namespace.
     * <p>
     * This is an optional attribute, the default name is <code>&lt;BuilderClass&gt;</code>
     * with an "s" appended to the end of the name
     * (the Builder class's name is <code>&lt;TargetClass&gt;Builder</code> by default,
     * but can be changed with the {@link Builder#className} attribute) -
     * so, if we're building a <code>Person</code> class, the name will be <code>PersonBuilders</code>.
     */
    String outerName() default "";

    /**
     * Allows you to set the Java package that the generated interfaces will reside in.
     * This is an optional attribute -
     * the default is for the interfaces to reside in the same package as the generated Builder class.
     *
     * @see Builder#packageName
     */
    String packageName() default "";

    /**
     * Allows you to change the names of the per-property inner interfaces that will be generated to enforce type safety.
     * Because this one attribute is used to control the names of interfaces for every property of the built class,
     * the value you provide can include the '*' character, which will be substituted with the
     * (capitalized) name of the property corresponding to that interface
     * (so, if the built class has a <code>name</code> property,
     * and you set this attribute to <code>"Jilt*"</code>,
     * the interface for the <code>name</code> property will be generated with the name <code>JiltName</code>).
     * <p>
     * This is an optional attribute -
     * the default name for the inner interfaces is simply the capitalized name of the property
     * (so, the same as the value <code>"*"</code>).
     */
    String innerNames() default "";

    /**
     * Allows you to set the name of the last generated inner interface -
     * the one that contains the <code>build</code> method,
     * which you invoke to obtain an instance of the target class after
     * setting all the needed properties on the Builder.
     * <p>
     * This is an optional attribute -
     * by default, the name is <code>Optionals</code> for Builders generated with
     * {@link Builder#style} set to {@link BuilderStyle#STAGED},
     * and <code>Build</code> for those with that attribute set to
     * {@link BuilderStyle#STAGED_PRESERVING_ORDER}.
     *
     * @see Builder#buildMethod
     */
    String lastInnerName() default "";
}
