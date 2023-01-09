Version 1.2 - 2023-01-08
------------------------
- Allow using Jilt with Java 9+ ([Pull Request #7](https://github.com/skinny85/jilt/pull/7))
- Fix wrong package name when compiling with Eclipse JDT ([Pull Request #8](https://github.com/skinny85/jilt/pull/8))

Version 1.1 - 2018-03-31
------------------------
- `@BuilderInterfaces` annotation with `outerName`, `packageName`, `innerNames` and `lastInnerName`
  attributes

Version 1.0 - 2017-06-01
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

Version 0.1 (initial Beta release) - 2016-12-01
-----------------------------------------------
- `@Builder` annotation that can be placed on classes only
- `@Builder.variant` attribute, and the `BuilderVariant` enum (with 2 values)
- `@Builder.optionalProperties` attribute as an Array of `String`s
