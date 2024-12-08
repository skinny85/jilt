package org.jilt.test.data.annotations.type;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Map;

/**
 * This class is used as an example of placing a type-use only annotation
 * on a Builder property.
 * While there isn't an explicit test using this class,
 * the fact that it compiles is test enough.
 */
public final class TypeUseOnlyExample {
    public final Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> entry;

    @Builder(style = BuilderStyle.STAGED)
    public TypeUseOnlyExample(Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> entry) {
        this.entry = entry;
    }
}
