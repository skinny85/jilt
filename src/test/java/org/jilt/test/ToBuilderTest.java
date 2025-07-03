package org.jilt.test;

import org.jilt.test.data.tobuilder.ToBuilderLombokData;
import org.jilt.test.data.tobuilder.ToBuilderLombokDataBuilder;
import org.jilt.test.data.tobuilder.ToBuilderLombokGetter;
import org.jilt.test.data.tobuilder.ToBuilderLombokGetterBuilder;
import org.jilt.test.data.tobuilder.ToBuilderValue;
import org.jilt.test.data.tobuilder.ToBuilderValueBuilder;
import org.jilt.test.data.tobuilder.User;
import org.jilt.test.data.tobuilder.UserBuilder;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ToBuilderTest {
    @Test
    public void test_to_builder() {
        ToBuilderValue value = new ToBuilderValue(1, Collections.singletonList("A"), 'a', true);
        ToBuilderValue builder = ToBuilderValueBuilder.toBuilder(value)
                .build();

        assertThat(value).isEqualTo(builder);
    }

    @Test
    public void only_a_single_required_property_can_be_set_with_to_builder_in_staged() {
        User original = UserBuilder.user()
                .email("email@example.com")
                .firstName("First")
                .lastName("Last")
                .build();
        User modified = UserBuilder.modifiedUser(original)
                .firstName("_changed_")
                .build();

        assertThat(modified.email).isEqualTo("email@example.com");
        assertThat(modified.username).isEqualTo("email@example.com");
        assertThat(modified.firstName).isEqualTo("_changed_");
        assertThat(modified.lastName).isEqualTo("Last");
        assertThat(modified.displayName).isEqualTo("First Last");
    }

    @Test
    public void test_to_builder_lombok_getter() {
        ToBuilderLombokGetter value = new ToBuilderLombokGetter("ToBuilderLombokGetter", 7, true, true);
        ToBuilderLombokGetter builder = ToBuilderLombokGetterBuilder.toBuilder(value).build();

        assertThat(value).isEqualTo(builder);
    }

    @Test
    public void test_to_builder_lombok_data() {
        ToBuilderLombokData value = new ToBuilderLombokData("ToBuilderLombokData", 7, true, true);
        ToBuilderLombokData builder = ToBuilderLombokDataBuilder.toBuilder(value).build();

        assertThat(value).isEqualTo(builder);
    }

}
