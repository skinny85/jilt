package org.jilt.test;

import org.jilt.test.data.constructor.MetaConstructorValue;
import org.jilt.test.data.constructor.MetaConstructorValueBuilder;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaAnnotationTest {
  @Test
  public void test__meta_builder_on_constructor() {
    MetaConstructorValue value = MetaConstructorValueBuilder.builder()
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
