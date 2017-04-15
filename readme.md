# Jilt

Jilt is a [Java annotation processor](https://docs.oracle.com/javase/8/docs/api/javax/annotation/processing/Processor.html)
used for automatically generating classes that implement the
[Builder design pattern](https://en.wikipedia.org/wiki/Builder_pattern#Java).

A simple example. Given this class:

```java
import org.jilt.Builder;

@Builder
public class Person {
    public final String name;
    public final int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

, Jilt will generate the following Builder code:

```java
public class PersonBuilder {
    private String name;
    private int age;
    
    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public PersonBuilder age(int age) {
        this.age = age;
        return this;
    }
    
    public Person build() {
        return new Person(name, age);
    }
}
```
