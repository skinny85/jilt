Version 1.8.3 (2025-07-22)
--------------------------
- Fix a bug where Lombok generated getters didn't work with `toBuilder`
  ([Issue #45](https://github.com/skinny85/jilt/issues/45))

Version 1.8.2 (2025-06-28)
--------------------------
- Fix a bug where the Functional Builder style did not work with generic classes

Version 1.8.1 (2025-04-20)
--------------------------
- Fix a bug where a recursive bound of a generic type would cause an infinite loop
  ([Issue #43](https://github.com/skinny85/jilt/issues/43))

Version 1.8 (2025-04-04)
------------------------
- Make Jilt an incremental annotation processor for Gradle
  ([Issue #35](https://github.com/skinny85/jilt/issues/35))
- Fix a bug where not all Functional Builder classes were correctly excluded from code coverage
  ([Issue #28](https://github.com/skinny85/jilt/issues/28))

Version 1.7 (2025-02-13)
------------------------
- Allow using `*` in `@Builder.className`
  ([Issue #39](https://github.com/skinny85/jilt/pull/39))
- Add `@JiltGenerated` annotation with `RetentionPolicy.CLASS`
  to all generated Builder classes ([Issue #28](https://github.com/skinny85/jilt/issues/28))
- Fix a bug where setting `@Builder.toBuilder` on a class with a private constructor
  would generate non-compiling code ([Issue #29](https://github.com/skinny85/jilt/issues/29))

Version 1.6.3 (2024-12-20)
--------------------------
- Fix a bug where the wrong `@Generated` annotation was used when passing `--release 8`
  to the compiler ([Issue #34](https://github.com/skinny85/jilt/pull/34))

Version 1.6.2 (2024-12-08)
--------------------------
- Fix a bug where type-use annotations were copied incorrectly to the setter methods
  ([Issue #32](https://github.com/skinny85/jilt/pull/32))

Version 1.6.1 (2024-06-20)
--------------------------
- Fix a bug where some annotations were copied twice to the setter methods
  ([Issue #22](https://github.com/skinny85/jilt/pull/22))

Version 1.6 (2024-06-15)
------------------------
- Add support for Functional Builders
  ([Issue #17](https://github.com/skinny85/jilt/issues/17))
- Fix a bug where boolean getters were not correctly recognized in
  `toBuilder` methods ([Issue #18](https://github.com/skinny85/jilt/issues/18))
- Propagate all annotations to setter methods, not only `@Nullable`
  ([Issue #20](https://github.com/skinny85/jilt/issues/20))

Version 1.5 (2024-03-23)
------------------------
- Add capability to generate a `toBuilder()` method
  ([Issue #16](https://github.com/skinny85/jilt/issues/16))
- Support meta-annotations ([Issue #14](https://github.com/skinny85/jilt/issues/14))
- Allow placing `@Builder` annotation on private constructors
  ([Issue #13](https://github.com/skinny85/jilt/issues/13))
- Fix a bug where JSpecify `@Nullable` annotations were not properly recognized
  ([Issue #11](https://github.com/skinny85/jilt/issues/11#issuecomment-2002620000))

Version 1.4 (2024-01-28)
------------------------
- Allow using Jilt with generic classes ([Issue #5](https://github.com/skinny85/jilt/issues/5))
- Use the term "Staged" instead of "Type-Safe" for this variant of the Builder pattern
  ([Issue #10](https://github.com/skinny85/jilt/issues/10))
- Make properties annotated with `@Nullable` annotations implicitly optional
  ([Issue #11](https://github.com/skinny85/jilt/issues/11))

Version 1.3 (2024-01-09)
------------------------
- Allow placing `@Builder` on Java 16+ `record` declarations ([Issue #9](https://github.com/skinny85/jilt/issues/9))

Version 1.2 (2023-01-08)
------------------------
- Allow using Jilt with Java 9+ ([Pull Request #7](https://github.com/skinny85/jilt/pull/7))
- Fix wrong package name when compiling with Eclipse JDT ([Pull Request #8](https://github.com/skinny85/jilt/pull/8))

Version 1.1 (2018-03-31)
------------------------
- `@BuilderInterfaces` annotation with `outerName`, `packageName`, `innerNames` and `lastInnerName`
  attributes ([Issue #1](https://github.com/skinny85/jilt/issues/1))

Version 1.0 (2017-06-01)
------------------------
- `@Builder` annotation that can be placed on classes, constructors and (static) methods
- `@Builder.style` attribute instead of `@Builder.variant`
- `BuilderStyle` enum (with 3 values)
- `@Opt` annotation instead of `@Builder.optionalProperties`
- additional properties on the `@Builder` annotation:
  - `className`
  - `packageName`
  - `setterPrefix`
  - `factoryMethod`
  - `buildMethod`
- `@Builder.Ignore` annotation

Version 0.1 (2016-12-01) - initial Beta release
-----------------------------------------------
- `@Builder` annotation that can be placed on classes only
- `@Builder.variant` attribute, and the `BuilderVariant` enum (with 2 values)
- `@Builder.optionalProperties` attribute as an Array of `String`s
