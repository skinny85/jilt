package org.jilt.test.data.defaultvalue;

import org.jilt.Builder;

import java.util.Set;

@Builder
public class DefaultValue {

    @Builder.Default("1")
    public int attr1;

    @Builder.Default("\"attr2\"")
    public String attr2;

    @Builder.Default("true")
    public boolean attr3;

    @Builder.Default("1000")
    public int attr4;

    @Builder.Default("java.util.Collections.emptySet()")
    public Set<String> attrs;

    public DefaultValue(int attr1, String attr2, boolean attr3, int attr4, Set<String> attrs) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
        this.attrs = attrs;
    }
}
