package org.jilt.test.data.defaultvalue;

import lombok.AllArgsConstructor;
import org.jilt.Builder;

import java.util.Set;

import static lombok.Builder.Default;

@Builder
@AllArgsConstructor
public class DefaultValueWithLombok {

    @lombok.Builder.Default
    public int attr1 = 1;

    @Default
    public String attr2 = "attr2";

    @lombok.Builder.Default
    public boolean attr3 = true;

    public int attr4 = 1000;

    @lombok.Builder.Default
    public int attr5 = attr1 + 1000;

    @lombok.Builder.Default
    public Set<String> attrs = java.util.Collections.emptySet();

    public Set<String> attrsWithNoDefault = java.util.Collections.emptySet();
}
