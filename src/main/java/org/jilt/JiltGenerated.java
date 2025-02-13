package org.jilt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker annotation with retention set to {@link RetentionPolicy#CLASS}
 * that is added to all Builder classes generated by Jilt.
 * The only purpose of this annotation is to mark classes it is found on as generated
 * (meaning, not handwritten) for tools like
 * <a href="https://www.jacoco.org/jacoco/trunk/doc">JaCoCo</a>.
 * While Jilt also adds the appropriate {@code @Generated} annotation
 * ({@code javax.annotation.Generated} when building on Java 8,
 * or {@code javax.annotation.processing.Generated} when using Java 9 or later)
 * to all code it generates,
 * that annotation has only {@link RetentionPolicy#SOURCE source code-level retention},
 * which means any tool that operates on the compiled class files won't have access to it.
 * Since {@link JiltGenerated} is retained in the class files,
 * it allows these tools to identify which classes were generated,
 * and automatically exclude them from their analysis.
 * <p>
 * Other than being a marker, this annotation has no effect,
 * and can be safely ignored.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface JiltGenerated {
}
