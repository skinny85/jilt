package org.jilt.test.data.typesafe.optional;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;
import org.jilt.Opt;

public class TypeSafeOptionalsValue {
    private final Integer opt1;
    private final Boolean opt2;
    private final Character opt3;
    private final double req1;
    private final String req2;
    private final Float opt4;
    private final String opt5;

    @Builder(style = BuilderStyle.TYPE_SAFE)
    @BuilderInterfaces(lastInnerName = "LastBuild")
    public TypeSafeOptionalsValue(@Opt Integer opt1, @Opt Boolean opt2, @Opt Character opt3,
                                  double req1, String req2, @Opt Float opt4, @Opt String opt5) {
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
