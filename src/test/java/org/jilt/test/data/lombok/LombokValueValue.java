package org.jilt.test.data.lombok;

import lombok.Value;
import org.jilt.Builder;

@Value
@Builder(toBuilder = "toBuilder")
public class LombokValueValue {
    int prop;
}
