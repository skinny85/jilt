package org.jilt.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.jilt.test.data.constructor.MetaConstuctorValue;
import org.jilt.test.data.constructor.MetaConstuctorValueBuilder;
import org.junit.Test;

public class MetaAnnotationTest {

  @Test
  public void test__meta_builder_on_constructor() {
    final MetaConstuctorValue value = MetaConstuctorValueBuilder.builder()
        .withAttr2("attr2_value")
        .withAttr4(4)
        .withAttr3(true)
        .build();

    assertThat(value.attr1).isEqualTo(123);
    assertThat(value.attr2).isEqualTo("attr2_value");
    assertThat(value.attr3).isTrue();
    assertThat(value.attr4).isEqualTo(4);
  }

}
