package org.jilt.test;

import org.jilt.test.data.functional.LargeLanguageModel;
import org.jilt.test.data.functional.TrickyNamesF;
import org.jilt.test.data.functional.TrickyNamesFuncBuilder;
import org.jilt.test.data.functional.UserFunc;
import org.jilt.test.data.functional.UserFuncBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jilt.test.data.functional.LargeLanguageModelBuilder.largeLanguageModel;
import static org.jilt.test.data.functional.LargeLanguageModelBuilder.name;
import static org.jilt.test.data.functional.LargeLanguageModelBuilder.outputTokensLimit;
import static org.jilt.test.data.functional.LargeLanguageModelBuilder.temperature;
import static org.jilt.test.data.functional.LargeLanguageModelBuilder.toBuilder;
import static org.jilt.test.data.functional.UserFuncBuilder.Optional.displayName;
import static org.jilt.test.data.functional.UserFuncBuilder.Optional.username;
import static org.jilt.test.data.functional.UserFuncBuilder.copy;
import static org.jilt.test.data.functional.UserFuncBuilder.email;
import static org.jilt.test.data.functional.UserFuncBuilder.firstName;
import static org.jilt.test.data.functional.UserFuncBuilder.lastName;
import static org.jilt.test.data.functional.UserFuncBuilder.userFunc;

public class FunctionalBuilderTest {
    @Test
    public void func_builder_for_all_required_properties_works() {
        LargeLanguageModel llm = largeLanguageModel(
                name(null),
                temperature(41F),
                outputTokensLimit(50)
        );

        assertThat(llm.name).isNull();
        assertThat(llm.temperature).isEqualTo(41);
        assertThat(llm.outputTokensLimit).isEqualTo(50);
    }

    @Test
    public void func_builder_with_only_required_properties_works() {
        UserFunc user = userFunc(
                email("my-email"),
                firstName("first-name"),
                lastName("last-name")
        );

        assertThat(user.email).isEqualTo("my-email");
        assertThat(user.username).isEqualTo("my-email");
        assertThat(user.firstName).isEqualTo("first-name");
        assertThat(user.lastName).isEqualTo("last-name");
        assertThat(user.displayName).isEqualTo("first-name last-name");
    }

    @Test
    public void func_builder_with_one_optional_property_works() {
        UserFunc user = UserFuncBuilder.userFunc(
                UserFuncBuilder.email("my-email"),
                UserFuncBuilder.firstName("first-name"),
                UserFuncBuilder.lastName("last-name"),
                UserFuncBuilder.Optional.displayName("display-name")
        );

        assertThat(user.email).isEqualTo("my-email");
        assertThat(user.username).isEqualTo("my-email");
        assertThat(user.firstName).isEqualTo("first-name");
        assertThat(user.lastName).isEqualTo("last-name");
        assertThat(user.displayName).isEqualTo("display-name");
    }

    @Test
    public void func_builder_all_properties_works() {
        UserFunc user = userFunc(
                email("my-email"),
                firstName("first-name"),
                lastName("last-name"),
                displayName("display-name"),
                username("username")
        );

        assertThat(user.email).isEqualTo("my-email");
        assertThat(user.username).isEqualTo("username");
        assertThat(user.firstName).isEqualTo("first-name");
        assertThat(user.lastName).isEqualTo("last-name");
        assertThat(user.displayName).isEqualTo("display-name");
    }

    @Test
    public void func_toBuilder_no_properties_works() {
        UserFunc user = userFunc(
                email("my-email"),
                firstName("first-name"),
                lastName("last-name")
        );
        UserFunc copy = copy(user);

        assertThat(copy.email).isEqualTo(user.email);
        assertThat(copy.username).isEqualTo(user.username);
        assertThat(copy.firstName).isEqualTo(user.firstName);
        assertThat(copy.lastName).isEqualTo(user.lastName);
        assertThat(copy.displayName).isEqualTo(user.displayName);
    }

    @Test
    public void func_builder_toBuilder_without_changes_works() {
        LargeLanguageModel llm = new LargeLanguageModel("my-name", 0.3F, 100);
        LargeLanguageModel copy = toBuilder(llm);

        assertThat(copy.name).isEqualTo("my-name");
        assertThat(copy.temperature).isEqualTo(0.3F);
        assertThat(copy.outputTokensLimit).isEqualTo(100);
    }

    @Test
    public void func_builder_toBuilder_with_one_required_prop_works() {
        LargeLanguageModel llm = new LargeLanguageModel("my-name", 0.3F, 100);
        LargeLanguageModel copy = toBuilder(llm, name("changed-name"));

        assertThat(copy.name).isEqualTo("changed-name");
        assertThat(copy.temperature).isEqualTo(0.3F);
        assertThat(copy.outputTokensLimit).isEqualTo(100);
    }

    @Test
    public void func_builder_toBuilder_with_all_props_works() {
        LargeLanguageModel llm = new LargeLanguageModel("my-name", 0.3F, 100);
        LargeLanguageModel copy = toBuilder(llm,
                outputTokensLimit(50), temperature(99F), name("changed-name"), temperature(20F));

        assertThat(copy.name).isEqualTo("changed-name");
        assertThat(copy.temperature).isEqualTo(20F);
        assertThat(copy.outputTokensLimit).isEqualTo(50);
    }

    @Test
    public void tricky_names_builder_works() {
        TrickyNamesF trickyNames = TrickyNamesFuncBuilder.trickyNamesF(
                TrickyNamesFuncBuilder.setter("setter"),
                TrickyNamesFuncBuilder.optional(true),
                TrickyNamesFuncBuilder.builder('b'),
                TrickyNamesFuncBuilder.trickyNamesFBuilder(11.0),
                TrickyNamesFuncBuilder.Optional.optValue(13)
        );

        assertThat(trickyNames.setter).isEqualTo("setter");
        assertThat(trickyNames.optional).isTrue();
        assertThat(trickyNames.builder).isEqualTo('b');
        assertThat(trickyNames.trickyNamesFBuilder).isEqualTo(11.0);
        assertThat(trickyNames.optValue).isEqualTo(13);
    }

    @Test
    public void tricky_names_toBuilder_works() {
        TrickyNamesF trickyNames = TrickyNamesFuncBuilder.trickyNamesF(
                TrickyNamesFuncBuilder.setter("setter"),
                TrickyNamesFuncBuilder.optional(true),
                TrickyNamesFuncBuilder.builder('b'),
                TrickyNamesFuncBuilder.trickyNamesFBuilder(11.0)
        );
        TrickyNamesF modifiedTrickyNames = TrickyNamesFuncBuilder.trickyToBuilder(trickyNames,
                TrickyNamesFuncBuilder.Optional.optValue(13),
                TrickyNamesFuncBuilder.builder('c'));

        assertThat(modifiedTrickyNames.setter).isEqualTo("setter");
        assertThat(modifiedTrickyNames.optional).isTrue();
        assertThat(modifiedTrickyNames.builder).isEqualTo('c');
        assertThat(modifiedTrickyNames.trickyNamesFBuilder).isEqualTo(11.0);
        assertThat(modifiedTrickyNames.optValue).isEqualTo(13);
    }
}
