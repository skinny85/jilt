package org.jilt.test.data.annotations.type;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Map;

/**
 * This class is used as an example of placing a type-use only annotation
 * on a Builder property.
 */
public final class TypeUseExample {
    public final Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> entry;

    @Builder(style = BuilderStyle.STAGED)
    public TypeUseExample(Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> entry) {
        this.entry = entry;
    }
}
