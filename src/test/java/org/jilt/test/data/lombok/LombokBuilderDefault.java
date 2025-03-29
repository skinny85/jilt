package org.jilt.test.data.lombok;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

import java.util.Set;

@Builder(style = BuilderStyle.STAGED, toBuilder = "toBuilder")
@AllArgsConstructor
@Getter
public final class LombokBuilderDefault {
    @Default
    private int nr = 1;

    @Default
    @Opt // redundant, but doesn't hurt
    private String strNrPlus1k = String.valueOf(this.nr + 1_000);

    // this initializer is ignored
    private char charNoDefaultWithInit = 'c';

//    @Default
//    private final boolean boolDefaultNoInit;

    @Default
    private Set<String> strings = java.util.Collections.singleton("s");

    @Opt
    // this initializer is ignored
    private String optAttr = java.util.Collections.singletonList("opt").toString();
}
