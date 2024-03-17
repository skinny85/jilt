package org.jilt.test.data.record;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

@Builder(style = BuilderStyle.STAGED, toBuilder = "toBuilder")
public record RecordNoWorkaround(@Opt String name, int age) {
}
