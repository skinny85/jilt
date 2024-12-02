package org.jilt.test.data.defaultvalue;

import org.jilt.Builder;
import org.jilt.Opt;

import java.util.Set;

@Builder
public class DefaultValueWithOpt {

    @Opt
    public int attr1 = 1;

    @Opt
    public String attr2 = "attr2";

    @Opt
    public boolean attr3 = true;

    public int attr4 = 1000;

    @lombok.Builder.Default
    public int attr5 = 1000;

    @Opt
    public Set<String> attrs = java.util.Collections.emptySet();

    public Set<String> attrsWithNoDefault = java.util.Collections.emptySet();


    public DefaultValueWithOpt(int attr1, String attr2, boolean attr3, int attr4, int attr5, Set<String> attrs, Set<String> attrsWithNoDefault) {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
        this.attr4 = attr4;
        this.attr5 = attr5;
        this.attrs = attrs;
        this.attrsWithNoDefault = attrsWithNoDefault;
    }
}
