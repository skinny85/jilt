package org.jilt.test;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class JavadocTest extends AbstractJavadocTest {
    @Test
    public void classic_Builder_on_classes_copies_field_Javadocs_to_Builder_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/classic/ClassicValueBuilder.java", "securityAnswers");
        assertThat(setterJavadoc).hasValue("Example JavaDoc for field {@link #securityAnswers}.\n");
    }

    @Test
    public void staged_Builder_on_constructors_copies_param_tag_Javadocs_to_Builder_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/tobuilder/UserBuilder.java", "lastName");
        assertThat(setterJavadoc).hasValue("example JavaDoc for lastName parameter\n");
    }

    @Test
    public void staged_Builder_on_constructors_copies_param_tag_Javadocs_to_interface_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/tobuilder/UserBuilders.java", "LastName", "lastName");
        assertThat(setterJavadoc).hasValue("example JavaDoc for lastName parameter\n");
    }

    @Test
    public void staged_ordered_Builder_on_static_methods_copies_param_tag_Javadocs_to_Builder_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/generic/Generic3TypeParamsBuilder.java", "b");
        assertThat(setterJavadoc).hasValue("example JavaDoc for parameter b\n          (with multiple lines)\n");
    }

    @Test
    public void staged_ordered_Builder_on_static_methods_copies_param_tag_Javadocs_to_interface_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/generic/Generic3TypeParamsBuilders.java", "B", "b");
        assertThat(setterJavadoc).hasValue("example JavaDoc for parameter b\n          (with multiple lines)\n");
    }

    @Test
    public void functional_Builder_on_classes_copies_field_Javadocs_to_Builder_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/functional/LargeLanguageModelBuilder.java", "name");
        assertThat(setterJavadoc).hasValue("example JavaDoc of the {@link LargeLanguageModel#name} field\n");
    }
}
