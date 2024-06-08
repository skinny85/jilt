package org.jilt.test;

import org.jilt.test.data.functional.LargeLanguageModel;
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
}
