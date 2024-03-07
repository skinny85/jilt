package org.jilt.test.data.constructor;

import org.jilt.test.data.annotations.MetaBuilder;

public class MetaConstructorValue {
  public final int attr1;
  public final String attr2;
  public final boolean attr3;
  public final int attr4;

  @MetaBuilder
  public MetaConstructorValue(String attr2, int attr4, boolean attr3) {
    attr1 = 123;
    this.attr2 = attr2;
    this.attr3 = attr3;
    this.attr4 = attr4;
  }
}
