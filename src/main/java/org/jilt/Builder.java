package org.jilt;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Builder annotation marks a class, constructor or static method
 * for Builder class generation.
 * <p>
 * If you're not familiar with the Builder design pattern, the
 * <a href="https://en.wikipedia.org/wiki/Builder_pattern#Java">Wikipedia article on the subject</a>
 * is a good starting point.
 * <p>
 * When the @Builder annotation is present on an element, the Jilt annotation processor
 * will pick it up, and generate classes that implement the Builder design pattern.
 * What exactly will it generate depends on where was the annotation placed:
 *
 * <ul>
 *     <li>
 *          You can place it directly on the class you want to generate the Builder for.
 *          In that case, the properties of the Builder will be all of the instance fields
 *          of the annotated class (you can use the {@link Builder.Ignore} annotation to
 *          exclude any fields you don't want present in the resulting Builder), in the order
 *          they were declared in the class. The processor expects that the class will have a
 *          constructor taking all of the (non-Ignored) fields as parameters (in the same order
 *          they were declared in the class), and it will use it in the <code>build</code> method
 *          to construct an instance of the class.
 *          <p>
 *          For example, given this code:
 *
 *          <pre><code>
 *     {@literal @}Builder
 *      public final class Person {
 *          public final String name;
 *          public final boolean isAdult;
 *
 *         {@literal @}Builder.Ignore
 *          private String socialSecurityNumber = "***-**-****";
 *
 *          public Person(String name, boolean isAdult) {
 *              this.name = name;
 *              this.isAdult = isAdult;
 *          }
 *      }
 *          </code></pre>
 *
 *          ...Jilt will generate the following Builder class:
 *
 *          <pre><code>
 *      public class PersonBuilder {
 *          public static PersonBuilder person() {
 *              return new PersonBuilder();
 *          }
 *
 *          private String name;
 *          private boolean isAdult;
 *
 *          public PersonBuilder name(String name) {
 *              this.name = name;
 *              return this;
 *          }
 *
 *          public PersonBuilder isAdult(boolean isAdult) {
 *              this.isAdult = isAdult;
 *              return this;
 *          }
 *
 *          public Person build() {
 *              return new Person(name, isAdult);
 *          }
 *      }
 *          </code></pre>
 *      </li>
 *
 *      <li>
 *          You can place it on a constructor of the class you want to generate the Builder for.
 *          In that case, the properties of the Builder will be all of the constructor parameters
 *          (in the same order they were declared in the constructor). The <code>build</code>
 *          method will call the annotated constructor to obtain an instance of the target class.
 *          <p>
 *          For example, given this code:
 *
 *          <pre><code>
 *      public final class Person {
 *          public final String name;
 *          public final boolean isAdult;
 *
 *         {@literal @}Builder
 *          public Person(String firstName, String lastName, int age) {
 *              this.name = firstName + " " + lastName;
 *              this.isAdult = age &gt; 20;
 *          }
 *      }
 *          </code></pre>
 *
 *          ...Jilt will generate the following Builder class:
 *
 *          <pre><code>
 *     public class PersonBuilder {
 *          public static PersonBuilder person() {
 *              return new PersonBuilder();
 *          }
 *
 *          private String firstName;
 *          private String lastName;
 *          private int age;
 *
 *          public PersonBuilder firstName(String firstName) {
 *              this.firstName = firstName;
 *              return this;
 *          }
 *
 *          public PersonBuilder lastName(String lastName) {
 *              this.lastName = lastName;
 *              return this;
 *          }
 *
 *          public PersonBuilder age(int age) {
 *              this.age = age;
 *              return this;
 *          }
 *
 *          public Person build() {
 *              return new Person(firstName, lastName, age);
 *          }
 *      }
 *          </code></pre>
 *      </li>
 *
 *      <li>
 *          You can place it on a (static) method. In that case, the processor will generate
 *          a Builder for the class that is the return type of that method, and the properties
 *          of the Builder will be all of the method parameters (in the same order they were
 *          declared in the method). The <code>build</code> method will call the annotated method
 *          to obtain an instance of the target class.
 *          <p>
 *          This is especially useful in cases you don't want to or can't modify
 *          the source code of the class you are generating the Builder for.
 *          <p>
 *          For example, given this code:
 *
 *          <pre><code>
 *      public final class Person {
 *          public final String name;
 *          public final boolean isAdult;
 *
 *          Person(String name, boolean isAdult) {
 *              this.name = name;
 *              this.isAdult = isAdult;
 *          }
 *      }
 *
 *      public abstract class PersonFactory {
 *         {@literal @}Builder
 *          public static Person make(String firstName, String lastName, int age) {
 *              return new Person(firstName + " " + lastName, age &gt; 20);
 *          }
 *      }
 *          </code></pre>
 *
 *          ...Jilt will generate the following Builder class:
 *
 *          <pre><code>
 *     public class PersonBuilder {
 *          public static PersonBuilder person() {
 *              return new PersonBuilder();
 *          }
 *
 *          private String firstName;
 *          private String lastName;
 *          private int age;
 *
 *          public PersonBuilder firstName(String firstName) {
 *              this.firstName = firstName;
 *              return this;
 *          }
 *
 *          public PersonBuilder lastName(String lastName) {
 *              this.lastName = lastName;
 *              return this;
 *          }
 *
 *          public PersonBuilder age(int age) {
 *              this.age = age;
 *              return this;
 *          }
 *
 *          public Person build() {
 *              return PersonFactory.make(firstName, lastName, age);
 *          }
 *      }
 *          </code></pre>
 *      </li>
 * </ul>
 *
 * There are different flavors of Builders that can be generated for the same class.
 * The {@link #style} attribute controls what is the variant used in each particular case.
 * <p>
 * A field or constructor/static method parameter can be marked with the {@link Opt} annotation,
 * which means the Builder property extracted from that field or parameter will be optional.
 * See the {@link Opt} annotation documentation for details.
 * <p>
 * The @Builder annotation has also a bunch of (optional) attributes that allow you to
 * customize the generated Builder class:
 *
 * <ul>
 *     <li>{@link #className}</li>
 *     <li>{@link #packageName}</li>
 *     <li>{@link #setterPrefix}</li>
 *     <li>{@link #factoryMethod}</li>
 *     <li>{@link #buildMethod}</li>
 * </ul>
 *
 * @since 0.1
 * @see Builder.Ignore
 * @see Opt
 * @see BuilderStyle
 * @see #style
 * @see #className
 * @see #packageName
 * @see #setterPrefix
 * @see #factoryMethod
 * @see #buildMethod
 * @see BuilderInterfaces
 */
@Documented
@Target({ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Builder {
    /**
     * Allows declaring what variant of the Builder pattern will the generated class conform to.
     * See {@link BuilderStyle} for the possible values.
     * <p>
     * This is an optional attribute - the default is {@link BuilderStyle#CLASSIC}.
     *
     * @see BuilderStyle
     */
    BuilderStyle style() default BuilderStyle.CLASSIC;

    /**
     * Allows declaring what should be the name of the generated Builder class.
     * Must be a valid Java (top-level) class name.
     * <p>
     * This is an optional attribute - the default is <code>&lt;TargetClass&gt;Builder</code>
     * (so, for example, if we're building an instance of a <code>Person</code> class,
     * the default name will be <code>PersonBuilder</code>).
     */
    String className() default "";

    /**
     * Allows declaring what package should the generated Builder class reside in.
     * Must be a valid Java package name.
     * <p>
     * This is an optional attribute - the default is for the Builder class to reside in
     * the same package as the class it builds.
     */
    String packageName() default "";

    /**
     * Allows adding a prefix to the setter methods of the generated Builder.
     * The final name of the setter is calculated by concatenating the prefix
     * and the capitalized property name (unless the prefix is empty, in which
     * case the property name is used as-is).
     * <p>
     * For example, if you set this attribute to <code>"with"</code>, and the target
     * class has a property <code>name</code>, the generated Builder will have
     * a setter called <code>withName</code>.
     * <p>
     * This is an optional attribute - the default is to have an empty prefix,
     * in which case the setter name will be exactly as the property name
     * (so, in the above example, the setter would be called <code>name</code>).
     */
    String setterPrefix() default "";

    /**
     * Allows changing the name of the static factory method present in the generated Builder.
     * <p>
     * The generated Builder code always includes a static factory method.
     * This, combined with Java's static imports, allows you to write nice-looking code like:
     *
     * <pre><code>
     *     // imagine Person is a class we are generating a Builder for
     *     import org.example.Person;
     *     import static org.example.PersonBuilder.person;
     *
     *     // later, in code:
     *     Person person = person()
     *          .name("Some name")
     *          .age(21)
     *          .build();
     * </code></pre>
     *
     * This attribute allows you to change the name of the generated static factory method.
     * It must be a valid Java method name.
     * <p>
     * This is an optional attribute - the default is for the static factory method name to
     * be the un-capitalized name of the class being built (so, if we're building a
     * <code>Person</code>, the default factory method name would be <code>person</code>).
     */
    String factoryMethod() default "";

    /**
     * Allows changing the name of the 'build' method (the method used in the last step of
     * building to actually obtain an instance of the built class).
     * Must be a valid Java method name.
     * <p>
     * This is an optional attribute - the default name is <code>build</code>.
     */
    String buildMethod() default "";

    /**
     * Allows generating a static method in the Builder class that creates a new instance of it,
     * initialized with values from a provided instance of the built class.
     * This is useful for easily creating copies of the built class with only a few properties changed,
     * while still keeping the original class immutable.
     * <p>
     * The value of this attribute is the name to use for the generated method.
     * The default is the empty string, which means no method will be generated.
     */
    String toBuilder() default "";

    /**
     * Annotation that ignores the given field of a class when generating a Builder for that class.
     * Used when {@link Builder} is placed on the class being built itself.
     *
     * @since 1.0
     * @see Builder
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @interface Ignore {
    }

}
