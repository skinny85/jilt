package org.jilt.utils;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;

public final class Annotations {

  private final Builder builder;
  private final BuilderInterfaces builderInterfaces;

  public Annotations(final Builder builder, final BuilderInterfaces builderInterfaces) {
    this.builder = builder;
    this.builderInterfaces = builderInterfaces;
  }

  public Builder getBuilder() {
    return builder;
  }

  public BuilderInterfaces getBuilderInterface() {
    return builderInterfaces;
  }

}
