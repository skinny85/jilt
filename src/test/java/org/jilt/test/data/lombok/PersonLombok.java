package org.jilt.test.data.lombok;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NonNull;
import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Getter
@AllArgsConstructor
@Builder(style = BuilderStyle.STAGED)
public final class PersonLombok {
    @NonNull
    private String name;

    @Default
    private int age = 21;
}
