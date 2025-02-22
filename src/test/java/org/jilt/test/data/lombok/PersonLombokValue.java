package org.jilt.test.data.lombok;

import lombok.AccessLevel;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Value
@Builder(style = BuilderStyle.STAGED)
@lombok.Builder(access = AccessLevel.PRIVATE)
public class PersonLombokValue {
    @NonNull
    String name;

    @Default
    int age = 21;
}
