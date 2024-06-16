package org.jilt.test.data.functional;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

@Builder(style = BuilderStyle.FUNCTIONAL, toBuilder = "trickyToBuilder", className = "TrickyNamesFuncBuilder")
@BuilderInterfaces(innerNames = "*SetInterf", lastInnerName = "OptionalF")
public class TrickyNamesF {
    public final String setter;
    public final boolean optional;
    @Opt public final int optValue;
    public final char builder;
    public final double trickyNamesFBuilder;

    public TrickyNamesF(String setter, boolean optional, int optValue, char builder, double trickyNamesFBuilder) {
        this.setter = setter;
        this.optional = optional;
        this.optValue = optValue;
        this.builder = builder;
        this.trickyNamesFBuilder = trickyNamesFBuilder;
    }
}
