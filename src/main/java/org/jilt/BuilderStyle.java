package org.jilt;

/**
 * Enumerates the possible variants of the Builder pattern that the generated Builder class
 * will conform to. It is used as the type of the {@link Builder#style} attribute.
 * <p>
 * Variants differ between each other in the API that they generate for the Builder clients.
 * There are 3 variants to choose from:
 * <ul>
 *     <li>{@link #CLASSIC}</li>
 *     <li>{@link #TYPE_SAFE}</li>
 *     <li>{@link #TYPE_SAFE_UNGROUPED_OPTIONALS}</li>
 * </ul>
 *
 * @since 1.0
 * @see Builder
 * @see Builder#style
 * @see #CLASSIC
 * @see #TYPE_SAFE
 * @see #TYPE_SAFE_UNGROUPED_OPTIONALS
 */
public enum BuilderStyle {
    /**
     * The "standard" Builder variant. It will generate one class,
     * which will have a setter for each property that will return
     * <code>this</code> (a fluent interface), and a <code>build</code> method
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
     * A Type-Safe (also called Staged, or Telescopic) Builder pattern variant.
     * <p>
     * This variant is most often used for Builders with many properties.
     * It leverages the type system to make sure all of the required
     * properties are set before constructing an instance of the target class.
     * <p>
     * It generates a separate interface for each setter corresponding
     * to a required property, which returns the next properties'
     * interface, and so on, forming a chain of calls.
     * Optional properties are grouped into one interface that also
     * includes the <code>build</code> method, allowing the client to make
     * a decision whether to set them or not. The generated Builder
     * class implements all of those interfaces.
     * <p>
     * This is quite hard to describe, but easy to demonstrate with an
     * example. Given this class:
     *
     * <pre><code>
     * public final class User {
     *     public final String email, username, firstName, lastName, displayName;
     *
     *    {@literal @}Builder(style = BuilderStyle.TYPE_SAFE)
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
     * ...Jilt will generate all of the following code:
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
     *         .displayName("johnnyd") // this could be 'username', or skipped
     *         .build();
     * </code></pre>
     *
     * @see BuilderInterfaces
     */
    TYPE_SAFE,

    /**
     * A slightly modified version of the {@link #TYPE_SAFE} Builder pattern variant.
     * It's mostly useful in cases where the class has only a small number of optional properties.
     * <p>
     * The only difference between this variant and {@link #TYPE_SAFE} is the treatment
     * of optional properties (those originating from fields or parameters annotated with {@link Opt}) -
     * in other words, for Builders with only required properties,
     * both will generate equivalent code. While the {@link #TYPE_SAFE} variant groups
     * all optional properties into one interface that is obtained at the end of the build
     * process, this style maintains the same order of properties as they were declared in
     * (through the instance fields of a class or constructor/static methods parameters,
     * depending on where was the {@link Builder} annotation placed).
     * The way properties are made optional is that they can be 'skipped' when building
     * the target instance. This is achieved by the interfaces for each optional property having
     * more than one method - they include also all setters for consecutive properties
     * up to the first required one.
     * <p>
     * This is easier explained with an example. Given this code (same target class as for the
     * {@link #TYPE_SAFE} variant):
     *
     * <pre><code>
     * public final class User {
     *     public final String email, username, firstName, lastName, displayName;
     *
     *    {@literal @}Builder(style = BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS)
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
     * ...Jilt will generate all of the following code:
     *
     * <pre><code>
     * public interface UserBuilders {
     *     interface Email {
     *         Username email(String email);
     *     }
     *
     *     interface Username {
     *         FirstName username(String username);
     *         LastName firstName(String firstName);
     *     }
     *
     *     interface FirstName {
     *         LastName firstName(String firstName);
     *     }
     *
     *     interface LastName {
     *         DisplayName lastName(String lastName);
     *     }
     *
     *     interface DisplayName {
     *         Build displayName(String displayName);
     *         User build();
     *     }
     *
     *     interface Build {
     *         User build();
     *     }
     * }
     *
     * public class UserBuilder implements UserBuilders.Email, UserBuilders.Username, UserBuilders.FirstName,
     *         UserBuilders.LastName, UserBuilders.DisplayName, UserBuilders.Build {
     *     public static UserBuilders.Email user() {
     *         return new UserBuilder();
     *     }
     *
     *     private String email, username, firstName, lastName, displayName;
     *
     *     private UserBuilder() {
     *     }
     *
     *     public UserBuilders.Username email(String email) {
     *         this.email = email;
     *         return this;
     *     }
     *
     *     public UserBuilders.FirstName username(String username) {
     *         this.username = username;
     *         return this;
     *     }
     *
     *     public UserBuilders.LastName firstName(String firstName) {
     *         this.firstName = firstName;
     *         return this;
     *     }
     *
     *     public UserBuilders.DisplayName lastName(String lastName) {
     *         this.lastName = lastName;
     *         return this;
     *     }
     *
     *     public UserBuilders.Build displayName(String displayName) {
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
     *         .email("email@example.com") // this has to be 'email' to compile - required property
     *         .username("johnnyd") // this line could be commented out - optional property
     *         .firstName("John") // this has to be 'firstName' to compile - required property
     *         .lastName("Doe") // this has to be 'lastName' to compile - required property
     *         .displayName("Unknown") // this line could be commented out - optional property
     *         .build();
     * </code></pre>
     *
     * @see BuilderInterfaces
     */
    TYPE_SAFE_UNGROUPED_OPTIONALS
}
