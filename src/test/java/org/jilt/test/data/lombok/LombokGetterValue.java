package org.jilt.test.data.lombok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jilt.Builder;

@Builder(toBuilder = "toBuilder", packageName = "org.jilt.test.data.lombok.custom")
@RequiredArgsConstructor
@Getter
public final class LombokGetterValue {
    private final String str;
    final boolean smallBool;
    private final Boolean bigBool;
}
