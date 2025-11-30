package org.jilt.test;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordJavadocTest extends AbstractJavadocTest {
    @Test
    public void staged_Builder_on_records_copies_param_tag_Javadocs_to_Builder_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/record/RecordNoWorkaroundBuilder.java", "age");
        assertThat(setterJavadoc).hasValue("Example JavaDoc of the {@code age} parameter.\n");
    }

    @Test
    public void staged_Builder_on_records_copies_param_tag_Javadocs_to_interface_setters() throws FileNotFoundException {
        Optional<String> setterJavadoc = this.getSetterJavadoc("org/jilt/test/data/record/RecordNoWorkaroundBuilders.java", "Age", "age");
        assertThat(setterJavadoc).hasValue("Example JavaDoc of the {@code age} parameter.\n");
    }
}
