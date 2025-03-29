package org.jilt.test.data.functional;

import org.jilt.Builder;
import org.jilt.BuilderInterfaces;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.FUNCTIONAL, toBuilder = "toBuilder", buildMethod = "create")
@BuilderInterfaces(outerName = "LargeLanguageModelBuilderInterfaces")
public final class LargeLanguageModel {
    public final String name;
    public final float temperature;
    public final int outputTokensLimit;

    public LargeLanguageModel(String name, float temperature, int outputTokensLimit) {
        this.name = name;
        this.temperature =  temperature;
        this.outputTokensLimit = outputTokensLimit;
    }

    public String getName() {
        return name;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getOutputTokensLimit() {
        return outputTokensLimit;
    }
}
