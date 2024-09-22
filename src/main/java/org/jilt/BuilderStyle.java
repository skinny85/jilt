package org.jilt;

/**
 * Enumerates the possible variants of the Builder pattern that the generated Builder class
 * will conform to. It is used as the type of the {@link Builder#style} attribute.
 * <p>
 * Variants differ between each other in the API that they generate for the Builder clients.
 * There are 3 variants to choose from:
 * <ul>
 *     <li>{@link #CLASSIC}</li>
 *     <li>{@link #STAGED}</li>
 *     <li>{@link #STAGED_PRESERVING_ORDER}</li>
 * </ul>
 *
 * @since 1.0
 * @see Builder
 * @see Builder#style
 * @see #CLASSIC
 * @see #STAGED
 * @see #STAGED_PRESERVING_ORDER
 */
public enum BuilderStyle {
    /**
     * The "standard" Builder variant. It will generate one class,
     * which will have a setter for each property that will return
     * <code>this</code> (a fluent interface), and a <code>build()</code> method
     * that can be used at the end of the build process to obtain an
     * instance of the target class (how exactly is the instance
     * obtained depends on where was the {@link Builder} annotation placed).
     * <p>
     * For example, given the following code:
     *
     * <pre><code>
     *     {@literal @}Builder(style = BuilderStyle.CLASSIC)
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
     * </code></pre>
     *
     * ...Jilt will generate the following Builder class:
     *
     * <pre><code>
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
     * </code></pre>
     *
     * This is the default Builder style (the default value of the
     * {@link Builder#style} attribute).
     * <p>
     * <strong>Note</strong>: this variant does not have a notion of optional properties -
     * it completely ignores the {@link Opt} annotation.
     * The generated code for required and optional properties is exactly the same -
     * in other words, in the above example, you could annotate one or
     * both of the <code>Person</code> class fields with {@link Opt},
     * and the resulting code would be identical.
     */
    CLASSIC,

    /**
     * Synonym for {@link #STAGED}.
     * Historically, Jilt used to refer to this style of Builder classes as "Type-Safe".
     * However, in the time since the initial version of this library was released,
     * the community seems to have agreed on the name "Staged" for this concept instead.
     * <p>
     * This constant has been kept purely for backwards-compatibility reasons;
     * however, it is recommended to use {@link #STAGED} instead.
     */
    TYPE_SAFE,

    /**
     * Synonym for {@link #STAGED_PRESERVING_ORDER}.
     * Historically, Jilt used to refer to this style of Builder classes as "Type-Safe".
     * However, in the time since the initial version of this library was released,
     * the community seems to have agreed on the name "Staged" for this concept instead.
     * <p>
     * This constant has been kept purely for backwards-compatibility reasons;
     * however, it is recommended to use {@link #STAGED_PRESERVING_ORDER} instead.
     */
    TYPE_SAFE_UNGROUPED_OPTIONALS,

    /**
     * A Staged (also called Type-Safe, or Step, or Telescopic) Builder pattern variant.
     * <p>
     * This variant is most often used for Builders with many properties.
     * It leverages the type system to make sure all required
     * properties are set before constructing an instance of the target class.
     * <p>
     * It generates a separate interface for each setter corresponding
     * to a required property, which returns the next properties'
     * interface, and so on, forming a chain of calls.
     * Optional (that is, those annotated with the {@link Opt} annotation)
     * properties are grouped into one interface at the end of the chain that also
     * includes the <code>build()</code> method, allowing the client to make
     * a decision whether to set them or not.
     * The generated Builder class implements all of those interfaces.
     * <p>
     * For example, given this class:
     *
     * <pre><code>
     * public final class User {
     *     public final String email, username, firstName, lastName, displayName;
     *
     *    {@literal @}Builder(style = BuilderStyle.STAGED)
     *     public User(String email, @Opt String username, String firstName,
     *                 String lastName, @Opt String displayName) {
     *         this.email = email;
     *         this.username = username == null ? email : username;
     *         this.firstName = firstName;
     *         this.lastName = lastName;
     *         this.displayName = displayName == null
     *             ? firstName + " " + lastName
     *             : displayName;
     *     }
     * }
     * </code></pre>
     *
     * ...Jilt will generate the following code:
     *
     * <pre><code>
     * public interface UserBuilders {
     *     interface Email {
     *         FirstName email(String email);
     *     }
     *
     *     interface FirstName {
     *         LastName firstName(String firstName);
     *     }
     *
     *     interface LastName {
     *         Optionals lastName(String lastName);
     *     }
     *
     *     interface Optionals {
     *         Optionals username(String username);
     *
     *         Optionals displayName(String displayName);
     *
     *         User build();
     *     }
     * }
     *
     * public class UserBuilder implements UserBuilders.Email, UserBuilders.FirstName,
     *         UserBuilders.LastName, UserBuilders.Optionals {
     *     public static UserBuilders.Email user() {
     *         return new UserBuilder();
     *     }
     *
     *     private String email, username, firstName, lastName, displayName;
     *
     *     private UserBuilder() {
     *     }
     *
     *     public UserBuilders.FirstName email(String email) {
     *         this.email = email;
     *         return this;
     *     }
     *
     *     public UserBuilders.LastName firstName(String firstName) {
     *         this.firstName = firstName;
     *         return this;
     *     }
     *
     *     public UserBuilders.Optionals lastName(String lastName) {
     *         this.lastName = lastName;
     *         return this;
     *     }
     *
     *     public UserBuilders.Optionals username(String username) {
     *         this.username = username;
     *         return this;
     *     }
     *
     *     public UserBuilders.Optionals displayName(String displayName) {
     *         this.displayName = displayName;
     *         return this;
     *     }
     *
     *     public User build() {
     *         return new User(email, username, firstName, lastName, displayName);
     *     }
     * }
     * </code></pre>
     *
     * ...which can be used as follows:
     *
     * <pre><code>
     *     User user = UserBuilder.user()
     *         .email("email@example.com") // this has to be 'email' to compile
     *         .firstName("John") // this is not 'username', because that is an optional property
     *         .lastName("Doe") // this has to be 'lastName' to compile
     *         .displayName("Johnny D") // this line could be commented out - optional property
     *         .username("johnnyd") // this line could be commented out - optional property
     *         .build();
     * </code></pre>
     *
     * @see BuilderInterfaces
     * @since 1.4
     */
    STAGED,

    /**
     * A slightly modified version of the {@link #STAGED} Builder pattern variant.
     * It only differs from {@link #STAGED} in the treatment of optional properties
     * (those coming from fields or constructor/static method parameters annotated with {@link Opt}).
     * <p>
     * While the {@link #STAGED} variant groups all optional properties into one
     * interface that is obtained at the end of the build process
     * (after all the required properties have been set),
     * this style maintains the same order of properties as they were declared in
     * either the class as fields, or in a constructor/static method as parameters
     * (depending on where was the {@link Builder} annotation placed).
     * The way properties are made optional is that they can be 'skipped' when building the target instance.
     * This is achieved by the interfaces for each optional property having more than one method -
     * they include also all setters for consecutive properties up to (and including) the first next required one.
     * <p>
     * The advantage of this style over {@link #STAGED} is that it allows making a previously required property optional,
     * but still maintain backwards compatibility with existing code that already used the previously generated builder.
     * Because the {@link #STAGED} variant groups all optional properties into a single interface,
     * using it means only the last required property can be made optional without breaking backwards compatibility.
     * However, this style allows changing any required property to optional, not just the last one,
     * and that change won't affect any existing code written against the previously generated builder.
     * <p>
     * For example, given this code:
     *
     * <pre><code>
     * public final class FullName {
     *     public final String firstName, middleName, lastName;
     *
     *    {@literal @}Builder(style = BuilderStyle.STAGED)
     *     public FullName(String firstName, String middleName, String lastName) {
     *         this.firstName = firstName;
     *         this.middleName = middleName;
     *         this.lastName = lastName;
     *     }
     * }
     * </code></pre>
     *
     * If later we decide to make {@code middleName} optional,
     * that change would break the following code:
     *
     * <pre><code>
     *     FullName fullName = FullNameBuilder.fullName()
     *         .firstName("William")
     *         .middleName("H")
     *         .lastName("Macy")
     *         .build();
     * </code></pre>
     *
     * Since {@code middleName()} would now have to be called after {@code lastName()}, not before it.
     * <p>
     * However, if we use the {@code STAGED_PRESERVING_ORDER} style instead:
     *
     * <pre><code>
     * public final class FullName {
     *     public final String firstName, middleName, lastName;
     *
     *    {@literal @}Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
     *     public FullName(String firstName, @Opt String middleName, String lastName) {
     *         this.firstName = firstName;
     *         this.middleName = middleName;
     *         this.lastName = lastName;
     *     }
     * }
     * </code></pre>
     *
     * This code would still work:
     *
     * <pre><code>
     *     FullName fullName = FullNameBuilder.fullName()
     *         .firstName("William")
     *         .middleName("H")
     *         .lastName("Macy")
     *         .build();
     * </code></pre>
     *
     * And the following code would also work:
     *
     * <pre><code>
     *     FullName fullName = FullNameBuilder.fullName()
     *         .firstName("William")
     *         // no middleName() call here - optional property
     *         .lastName("Macy")
     *         .build();
     * </code></pre>
     *
     * Note that this style works best if either the built class has a small number of properties,
     * or if there is a natural order to those properties,
     * like in the {@code FullName} example above.
     * The reason why is that there is only a single spot where a given optional property can be set
     * (for example, {@code middleName()} above can only be called right after calling {@code firstName()}),
     * which might make it difficult to find if the class has a large amount of properties without an obvious order to them.
     * This is different from the {@link #STAGED} style,
     * where all optional properties can be set right before calling {@code build()},
     * and they can be set in any order, which makes them more easily discoverable.
     *
     * @see BuilderInterfaces
     * @since 1.4
     */
    STAGED_PRESERVING_ORDER,

    /**
     * A Functional variant of the Builder pattern.
     * It is inspired by a <a href="https://glaforge.dev/posts/2024/01/16/java-functional-builder-approach">blog article</a>
     * from Guillaume Laforge, creator of the Groovy programming language.
     * <p>
     * In this variant, an instance of a Builder is not explicitly used -
     * instead, a static factory method is generated on the Builder class
     * that directly returns an instance of the target class.
     * The arguments of that method are generated interfaces.
     * Each required property has its own interface,
     * while all optional properties share one interface.
     * <p>
     * Instances of these interfaces are obtained by calling static factory methods generated on the Builder class
     * (static methods for the optional properties are nested inside an additional {@code Optional} class,
     * for discoverability), and passing them the value for a given property.
     * Typically, you would use Java's static imports to bring those static methods directly into the scope that creates the instance.
     * <p>
     * For example, given this value class:
     *
     * <pre><code>
     * package example;
     *
     * public final class FullName {
     *     public final String firstName, middleName, lastName;
     *
     *    {@literal @}Builder(style = BuilderStyle.FUNCTIONAL)
     *     public FullName(String firstName, @Opt String middleName, String lastName) {
     *         this.firstName = firstName;
     *         this.middleName = middleName;
     *         this.lastName = lastName;
     *     }
     * }
     * </code></pre>
     *
     * You use the generated Builder like so:
     *
     * <pre><code>
     * import static example.FullNameBuilder.firstName;
     * import static example.FullNameBuilder.lastName;
     * import static example.FullNameBuilder.middleName;
     *
     * FullName fullName = FullNameBuilder.fullName(
     *     firstName("John"), // this is required
     *     lastName("Doe"),  // this is required
     *     middleName("Muriel") // this is optional
     * );
     * </code></pre>
     *
     * @since 1.6
     */
    FUNCTIONAL
}
