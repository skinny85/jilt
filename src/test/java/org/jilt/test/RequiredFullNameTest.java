package org.jilt.test;

import org.jilt.test.data.nullable.RequiredFullName;
import org.jilt.test.data.nullable.RequiredFullNameBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequiredFullNameTest {
    @Test
    public void nullable_annotation_combined_with_req_makes_property_required() {
        // NOTE: this would not compile if middleName was considered optional in a Staged Builder
        RequiredFullName fullName = RequiredFullNameBuilder.requiredFullName()
                .firstName("First")
                .middleName("Middle")
                .lastName("Last")
                .build();
        assertThat(fullName.firstName).isEqualTo("First");
        assertThat(fullName.middleName).isEqualTo("Middle");
        assertThat(fullName.lastName).isEqualTo("Last");
    }
}
