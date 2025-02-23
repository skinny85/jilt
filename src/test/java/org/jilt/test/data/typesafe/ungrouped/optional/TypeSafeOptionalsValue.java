package org.jilt.test.data.typesafe.ungrouped.optional;

import org.jilt.Builder;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class TypeSafeOptionalsValue {
    @Opt private final Integer opt1;
    @Opt private final Boolean opt2;
    @Opt private final Character opt3;
    private final double req1;
    private final String req2;
    @Opt private final Float opt4;
    @Opt private final String opt5;

    public TypeSafeOptionalsValue(Integer opt1, Boolean opt2, Character opt3, double req1, String req2, Float opt4, String opt5) {
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.req1 = req1;
        this.req2 = req2;
        this.opt4 = opt4;
        this.opt5 = opt5;
    }

    public Integer getOpt1() {
        return opt1;
    }

    public Boolean getOpt2() {
        return opt2;
    }

    public Character getOpt3() {
        return opt3;
    }

    public double getReq1() {
        return req1;
    }

    public String getReq2() {
        return req2;
    }

    public Float getOpt4() {
        return opt4;
    }

    public String getOpt5() {
        return opt5;
    }
}
