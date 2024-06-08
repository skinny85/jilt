# Jilt [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Download](https://maven-badges.herokuapp.com/maven-central/cc.jilt/jilt/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/cc.jilt/jilt) [![Build Status](https://github.com/skinny85/jilt/actions/workflows/build.yaml/badge.svg)](https://github.com/skinny85/jilt/actions/workflows/build.yaml)

Jilt is a [Java annotation processor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html)
used for automatically generating classes that implement the
[Builder design pattern](https://en.wikipedia.org/wiki/Builder_pattern#Java).

Jilt's "killer features" compared to other tools in this same space are:
* Support for the Staged (sometimes also called Type-Safe, or
    Step, or Telescopic) variant of the Builder pattern.
    For more information on the Staged Builder pattern, check out my
    [blog article on the subject](http://endoflineblog.com/type-safe-builder-pattern-in-java-and-the-jilt-library).
* The capability to generate Builders for any class,
    and without requiring any modifications to the target classes'
    source code.
* Seamless interoperability with other annotation processors, most
    noticeably [Lombok](https://projectlombok.org/).

Jilt is purely a code generator - it does not add any overhead,
nor any runtime dependencies, to your code.

#### Example

Given this class:

```java
import org.jilt.Builder;

@Builder
public final class Person {
    public final String name;
    public final boolean isAdult;

    public Person(String name, boolean isAdult) {
        this.name = name;
        this.isAdult = isAdult;
    }
}
```

...Jilt will generate the following Builder code:

```java
public class PersonBuilder {
    public static PersonBuilder person() {
        return new PersonBuilder();
    }

    private String name;
    private boolean isAdult;

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder isAdult(boolean isAdult) {
        this.isAdult = isAdult;
        return this;
    }

    public Person build() {
        return new Person(name, isAdult);
    }
}
```

Check out the [documentation below](#customizing-the-generated-code) for ways to customize what Jilt generates.

#### Getting Jilt

Jilt is available from the [Maven Central](https://search.maven.org/search?q=g:cc.jilt) repository.

Example Maven settings:

```xml
<dependencies>
    <dependency>
        <groupId>cc.jilt</groupId>
        <artifactId>jilt</artifactId>
        <version>1.5</version>
        <scope>provided</scope> <!-- Jilt is not needed at runtime -->
    </dependency>
</dependencies>
```

Example Gradle settings:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    // ...
    compileOnly 'cc.jilt:jilt:1.5' // Jilt is not needed at runtime
    annotationProcessor 'cc.jilt:jilt:1.5' // you might also need this dependency in newer Gradle versions
}
```

If you're not using dependency managers, you can
[download the JAR directly](https://repo1.maven.org/maven2/cc/jilt/jilt/1.5/jilt-1.5.jar)
(it's distributed as a self-contained JAR, you don't need any additional dependencies for it)
and add it to your classpath.

#### Customizing the generated code

##### @Builder on classes

When you place the `@Builder` annotation on the class itself,
the resulting Builder will have as properties all instance fields of that class
(you can mark a field with the `@Builder.Ignore` annotation to exclude it from being added to the Builder),
and will build the instance of that class assuming it has a constructor taking
all of those properties as arguments, in the same order they were declared in the class.
This allows you to easily use Jilt with [Lombok](https://projectlombok.org/);
for instance, the above example could have been rewritten as:

```java
import org.jilt.Builder;
import lombok.Data;

@Builder
@Data
public final class Person {
    private final String name;
    private final boolean isAdult;
}
```

##### @Builder on constructors

You can also place the annotation on a constructor;
in that case, the Builder properties will be the constructor parameters,
and the instance will be created by calling the constructor.
So, this code will produce the same Builder as the above example:

```java
import org.jilt.Builder;

public final class Person {
    public final String name;
    public final boolean isAdult;
    private int thisFieldWillBeIgnoredByTheBuilder;

    @Builder
    public Person(String name, boolean isAdult) {
        this.name = name;
        this.isAdult = isAdult;
    }
}
```

##### @Builder on static methods

Finally, you can also place the `@Builder` annotation on a (static) method.
In that case, the built class will be the return type of the method,
and the Builder properties will be the method parameters,
in the same order as they were declared in the method.
The instance will be created by making a call to the annotated method.

This is the most flexible way of generating Builders in Jilt -
you have full control of the code constructing the final instance,
which allows you to do things like:

* Generate Builders for classes without modifying their source code,
    or for classes that you don't control (from libraries, for example).
* Generate Builders for classes with non-standard ways to construct them
    (for example, those that use setters instead of constructor parameters).
* Customize the construction behavior - for example, add validations, or
    default property values.

Here is an example illustrating the possibilities:

```java
import org.jilt.Builder;

import java.util.Date;

public abstract class DateFactory {
    @Builder(packageName = "com.example")
    public static Date make(int month, int day, int year) {
        // validation
        if (month < 1 || month > 12)
            throw new IllegalArgumentException("month must be between 1 and 12");

        // default value
        if (day == 0)
            day = 1;

        // non-standard construction
        return new Date(year + 1900, month - 1, day);
    }
}
```

And you can use the generated Builder like so:

```java
import com.example.DateBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateFactoryTest {
    @Test
    public void use_date_builder() {
        Date date = DateBuilder.date()
            .month(12)
            .year(23)
            .build();

        Assert.assertEquals(11, date.getMonth());
        Assert.assertEquals(1, date.getDay());
        Assert.assertEquals(1923, date.getYear());
    }
}
```

##### Staged Builders

All Builders shown so far were "regular" Builders.
Using the `@Builder`'s `style` attribute, you can instead generate a
Staged (also called Type-Safe, or Step, or Telescopic) Builder by setting
`style` to `BuilderStyle.STAGED`.

A Staged Builder generates interfaces for each property of the Builder,
and enforces that they have to be initialized before constructing the final instance.
The order of construction will be exactly as the order of the properties in the Builder.

For a longer and more in-depth introduction to the Staged Builder pattern variant, check out my
[blog article on the subject](http://endoflineblog.com/type-safe-builder-pattern-in-java-and-the-jilt-library).

So, this slightly modified code from above:

```java
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED)
public final class Person {
    public final String name;
    public final boolean isAdult;

    public Person(String name, boolean isAdult) {
        this.name = name;
        this.isAdult = isAdult;
    }
}
```

...generates a Builder that can be only used as follows:

```java
Person person = PersonBuilder.person()
    .name("John Doe") // this has to be 'name' for the code to compile
    .isAdult(true) // this has to be 'isAdult' for the code to compile
    .build(); // this has to be 'build' for the code to compile
```

##### Optional properties

When using Staged Builders, there are often properties that don't have to be provided
in order to construct a valid instance of the target class -
the property could be optional, it could have some default, etc.

When using the `STAGED` Builder style, you can mark a field or constructor/static method parameter
(depending on where you placed the `@Builder` annotation) optional by annotating it with the
`@Opt` annotation. All optional Builder properties will be grouped into a single interface
(the same containing the `build` method), which means the client can (but doesn't have to)
provide them, after all the required properties have been set.
If a value for an optional property is not set, Jilt will construct the instance with
the zero-value for that property's type (`0` for `int` and other numeric types,`null` for reference types, etc.)
as the value of the property.

For example, a Builder for this class:

```java
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class User {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.STAGED)
    public User(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
            ? firstName + " " + lastName
            : displayName;
    }
}
```

...can be used as follows:

```java
User user = UserBuilder.user()
    .email("jd@example.com") // this has to be 'email' to compile
    .firstName("John") // this is not 'username', because that is an optional property
    .lastName("Doe") // this has to be 'lastName' to compile
    .displayName("Johnny D") // this could be 'username', or skipped
    .build();
```

In addition to the `@Opt` annotation,
a property will always be considered optional if the field or parameter it was generated from is annotated with a `@Nullable` annotation.
All types of `@Nullable` annotations are supported,
including `javax.annotation.Nullable` from [JSR-305](https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305),
`org.jetbrains.annotations.Nullable` from [JetBrains annotations](https://mvnrepository.com/artifact/org.jetbrains/annotations),
and others.

##### 'Staged, but preserving order' Builder style

The Staged Builder style has one downside:
when evolving your API, you cannot change a required property to be optional
(with the small exception of the last required property)
without breaking existing code that uses the Builder generated when the property was required -
even though, purely from an API perspective, that should not be a breaking change for the clients of your class.

For example, if we take the above `User` class, but with `username` being required,
the client code using that Builder looks something like this:

```java
User user = UserBuilder.user()
    .email("jd@example.com")
    .username("johnnyd") // username() has to be called here, as it's not optional
    .firstName("John")
    .lastName("Doe")
    .build();
```

However, if we then change `username` to be optional by annotating it with `@Opt`,
the above code will stop compiling,
because the `username()` call can no longer happen after the call to `email()`,
but must instead be moved to after the call to `lastName()`.

For this reason, there is one more Builder style - `STAGED_PRESERVING_ORDER`.
It's very similar to `STAGED` - it only differs in the treatment of optional properties.
Instead of bunching them all together at the end of the build process like `STAGED`,
this style retains the original order of the properties, but allows you to 'skip' setting
those that are optional, bypassing them and moving to the next required property.
This means that changing a required property to optional maintains backwards-compatibility
with any existing code that used the previously generated Builder.

For example, for the same code as the above `STAGED` example:

```java
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public final class User {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
    public User(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
            ? firstName + " " + lastName
            : displayName;
    }
}
```

...the generated Builder can be used like this:

```java
User user = UserBuilder.user()
    .email("jd@example.com") // this has to be 'email' to compile - required property
    .username("johnnyd") // this will always be where username is set, regardless whether it's required or optional
    .firstName("John") // this has to be 'firstName' to compile - required property
    .lastName("Doe") // this has to be 'lastName' to compile - required property
    .displayName("Johnny D") // this will always be where displayName is set, regardless whether it's required or optional
    .build();
```

Note that this style works best if either the class being built has a small number of properties,
or if there is a natural order to those properties, like in the `User` example above.
The reason why is that there is only a single spot where a given optional property can be set
(for example, `username()` above can only be called right after calling `email()`),
which might make it difficult to find if the class has a large amount of properties without an obvious order to them.
This is different from the `STAGED` style,
where all optional properties can be set right before calling `build()`,
and they can be set in any order, which makes them more easily discoverable.

##### Other @Builder attributes

In addition to`style`, the `@Builder` annotation has a bunch of attributes that allow you to control
practically all aspects of the generated Builder (all of them are optional):

* `className` allows you to change the generated Builder's name.
    The default name is `<BuiltClass>Builder`.
* `packageName` allows you to change the package the generated Builder will reside in.
    The default is for the Builder to be in the same package that the built class is in.
* `setterPrefix` allows you to add a prefix to the names of the generated setter methods.
    The default is to not have any prefix (the setter names will be the same as the property names).
* `factoryMethod` allows you to change the name of the generated static factory method for
    constructing Builder instances. The default is for the name to be equal to the un-capitalized
    name of the built class (for example, `person` when building a `Person` class).
* `buildMethod` allows you to change the name of the final method invoked on the Builder to
    obtain an instance of the built class. The default name of that method is `build`.
* `toBuilder` allows you to set the name of the static method in the Builder class that creates a new instance of it,
    initialized with values from the provided instance of the built class.
    This is useful for easily creating copies of the built class with only a few properties changed,
    while still keeping the original class immutable.
    The default value of this attribute is the empty string,
    which means this method will not be generated.

##### Functional Builder style

In addition to the Staged and 'Staged preserving order',
there is one more Builder style, called Functional.
It's inspired by a [blog article](https://glaforge.dev/posts/2024/01/16/java-functional-builder-approach)
from [Guillaume Laforge](https://twitter.com/glaforge),
creator of the [Groovy programming language](https://www.groovy-lang.org).

Functional Builders can be thought of as a way to implement named parameters,
a language feature that Java doesn't have.
Instead of a separate Builder instance that methods are called on,
Jilt generates a static factory method on the Builder class that returns an instance of the built class.
Arguments of that method are interfaces generated by Jilt that have a single `accept`
method that take an instance of the Builder,
and set one of the properties that will be used when creating the instance of the target class.
Each required property will have its own interface,
while optional properties all share the same interface.

Instances of those interfaces are obtained by calling other static methods of the Builder class,
with the same name as the name of the interface, which is the same as the name of the required property
(just uppercased in the case of the interface name).
For optional properties, since they share the same interface, the static methods are nested in one more interface,
called `Optional`, to make them more discoverable.
You pass the value of a given property to those static methods.

For an example, if we take the `User` class from above:

```java
package example;

public final class User {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.FUNCTIONAL, toBuilder = "toBuilder")
    public User(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
                ? firstName + " " + lastName
                : displayName;
    }
}
```

The generated Functional Builder can be used like this:

```java
User user = UserBuilder.user(
        UserBuilder.email("jd@example.com"), // this is required
        UserBuilder.firstName("John"), // this is required
        UserBuilder.lastName("Doe"), // this is required
        UserBuilder.Optional.displayName("Johnny D") // this is optional
);
```

If we use Java's [static imports](https://docs.oracle.com/javase/7/docs/technotes/guides/language/static-import.html)
feature, this becomes much more concise:

```java
import static example.UserBuilder.Optional.username;
import static example.UserBuilder.email;
import static example.UserBuilder.firstName;
import static example.UserBuilder.lastName;
import static example.UserBuilder.user;

User user = user(
        email("jd@example.com"), // this is required
        firstName("John"), // this is required
        lastName("Doe"), // this is required
        username("johnnyd") // this is optional
);
```

The Functional `toBuilder()` method works similarly,
but the difference is that all properties are now considered optional,
since we assume the required properties will be initialized from the passed target class instance:

```java
import static example.UserBuilder.Optional.displayName;

User user = new User(/* ... */);
User copy = UserBuilder.toBuilder(user,
        displayName("Johnny D") // only an optional property is fine here
);
```

##### @BuilderInterfaces annotation

When generating a Staged or Functional Builder
(so, when the `@Builder.style` attribute is set to anything other than `BuilderStyle.CLASSIC`,
which is the default),
you can also place the `@BuilderInterfaces` annotation on the same element `@Builder` is on
(so, a class, constructor, or static method).
This annotation is used to customize the interfaces generated to ensure the type-safety of the resulting Builder.
It has the following attributes (all of them are optional):

* `outerName` allows you to change the name of the outer interface that the per-property interfaces
  will be generated inside of (this is in order not to pollute the global namespace).
  The default name is `<BuilderClass>s` (the name of the Builder class with an "s" at the end) -
  so, if we're building a `Person` class, the default name will be `PersonBuilders`.
* `packageName` allows you to change the package the generated interfaces will reside in.
  The default is for the interfaces to reside in the same package as the one the Builder will be generated in.
* `innerNames` allows you to set the pattern that will be used for naming the per-property generated interfaces.
  The character `*` in the pattern will be substituted with the (capitalized) name of the property.
  The default name for the interfaces is simply the (capitalized) name of its corresponding property -
  so, the same as the pattern `"*"`.
* `lastInnerName` allows you to change the name of the final interface -
  the one containing the `build` method,
  which is invoked to obtain an instance of the target class.
  The default name for that interface is `Optionals` for `BuilderStyle.STAGED` Builders,
  and `Build` for `BuilderStyle.STAGED_PRESERVING_ORDER` ones.

##### Meta-annotations

In some cases, you may want to re-use the same Builder configuration for multiple classes.
For example, you might decide that every value class in your project should use a Staged Builder,
with "set" as the prefix for setter methods, "create" as the name of the `build` method,
and "B_" as the prefix of the per-property interface names used for the Builder stages.
In such situations, instead of repeating the same annotations in multiple places,
you can instead define your own annotation,
and annotate it with `@Builder` and `@BuilderInterfaces`:

```java
import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED, setterPrefix = "set", buildMethod = "create")
@BuilderInterfaces(innerNames = "B_*")
public @interface MyBuilder {
}
```

And then, you can place this `MyBuilder` so-called _meta annotation_ wherever `@Builder`
can be placed (so, a class, constructor, or static method),
and the effect will be as if that element was annotated with the same `@Builder`
and `@BuilderInterfaces` values as `@MyBuilder` is annotated with,
thus avoiding any duplication in your code:

```java
@MyBuilder // uses @Builder and @BuilderInterfaces values from @MyBuilder
public final class MyValueClass {
    // ...
}
```

##### Supporting classes with private constructors

In some cases, you might want to force customers of a class to only be able to instantiate it through its Builder,
and not through its constructor.
When writing the Builder code by hand,
you would achieve this by making the constructor of the class private,
and making Builder nested inside the main class.

Unfortunately, annotation processors cannot modify hand-written classes,
only create new ones
(yes, Lombok manages to sidestep this limitation,
but it does it in a way that is
[considered a hack](https://notatube.blogspot.com/2010/11/project-lombok-trick-explained.html)),
so Jilt cannot generate the Builder inside the main class.

Because of this, if you place `@Builder` on a private constructor or static factory method,
Jilt changes the generation behavior: the Builder class becomes abstract,
the fields are `protected` instead of `private`, and the `build` method becomes abstract too.
With this, you can extend the Builder class in a nested class of the main class and override the `build()`
method to call the private constructor,
using the fields of the parent class as values of the properties.
You can also provide a static factory method in your class that returns the Builder instance,
conventionally called just `builder()`,
which allows you to make the nested class private as well.

For example, if we wanted to make the constructor of the above `User` class private,
it would look something like this:

```java
public final class User {
    public final String email, username, firstName, lastName, displayName;

    @Builder(style = BuilderStyle.STAGED)
    private User(String email, @Opt String username, String firstName,
            String lastName, @Opt String displayName) {
        this.email = email;
        this.username = username == null ? email : username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.displayName = displayName == null
                ? firstName + " " + lastName
                : displayName;
    }

    private static class InnerBuilder extends UserBuilder {
        @Override
        public User build() {
            return new User(email, username, firstName, lastName, displayName);
        }
    }

    public static UserBuilders.Email builder() {
        return new InnerBuilder();
    }
}
```

With the above code, the only way to create an instance of `User`
would be to use the `User.builder()` static method,
and then instantiate it through the (Staged in this case) Builder.

#### Working in an IDE

Annotation processors can be a little tricky to get working correctly in an IDE.
Here are some tips for the most popular ones:

##### Intellij IDEA

Make sure to enable annotation processing
(`File` -> `Settings` -> `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`,
check the box that says `Enable annotation processing`).
Now, the code should run fine at this point, but you might still get errors in the IDE
because it can't find the sources for the generated Builder classes. In that case,
it might be a good idea to change the radio button labelled `Store generated sources relative to`
in that same menu screen from `Module output directory` (the default) to `Module content root`.
If you do that, make sure to mark the directories with the generated code as source directories
(right click on them in the file tree -> `Mark directory as` -> `Sources Root` or `Test Sources Root`,
the names of the directories are `generated` and `generated_tests` by default).

##### Eclipse

Eclipse is a lot more cumbersome. Right click on the project -> `Properties` -> `Java Compiler`
(expand the submenus) -> `Annotation Processing`, tick the `Enable project-specific settings` checkbox,
and then `Enable annotation processing`.
Then, go to the `Factory Path` submenu, tick the `Enable project-specific settings` checkbox there,
and add the Jilt JAR to the Factory path.
Make sure that the `Generated source directory` is marked as a Source Folder.

#### License

Jilt is open-source software, released under the Apache Version 2.0 License.
See the [License file](License.txt) for details.
