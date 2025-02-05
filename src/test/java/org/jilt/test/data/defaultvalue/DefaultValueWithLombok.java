package org.jilt.test.data.defaultvalue;

import lombok.AllArgsConstructor;
import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

import java.util.Set;

import static lombok.Builder.Default;

@Builder(style = BuilderStyle.STAGED)
@AllArgsConstructor
public class DefaultValueWithLombok {
    @Default
    public int nr = 1;

    @Default
    public String strNrPlus1k = String.valueOf(this.nr + 1_000);

    public char charNoDefaultWithInit = 'c';

    @Default
    public final boolean boolDefaultNoInit;

    @Default
    public Set<String> strings = java.util.Collections.emptySet();

    @Opt
    // ToDo - fix this so that it doesn't require a fully-qualified class name
    public String optAttr = java.util.Collections.singletonList("opt").toString();
}
