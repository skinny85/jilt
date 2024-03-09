package org.jilt.test.data.meta;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;

@BuilderInterfaces(lastInnerName = "Meta")
@Builder(setterPrefix = "with", factoryMethod = "builder", style = BuilderStyle.STAGED)
public @interface MetaBuilder {
}
