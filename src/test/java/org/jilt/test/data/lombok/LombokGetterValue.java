package org.jilt.test.data.lombok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

@Builder(toBuilder = "toBuilder")
@RequiredArgsConstructor
@Getter
public final class LombokGetterValue {
    private final String str;
    private final boolean smallBool;
    private final Boolean bigBool;
}
