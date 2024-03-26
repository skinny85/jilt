package com.jilt;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;

@BuilderInterfaces(innerNames = "My_*")
@Builder(setterPrefix = "set", toBuilder = "myToBuilder")
public @interface MyBuilder {
}
