package org.jilt.test.data.annotation_position;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

import java.util.Map;

public class TypeUseOnly {
    private final Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> typeUse;

    @Builder(style = BuilderStyle.STAGED)
    public TypeUseOnly(Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> typeUse) {
        this.typeUse = typeUse;
    }

    public Map.@TypeUseOnlyAnnotation Entry<Integer, Integer> getTypeUse() {
        return typeUse;
    }
}
