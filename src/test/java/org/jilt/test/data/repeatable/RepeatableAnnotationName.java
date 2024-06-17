package org.jilt.test.data.repeatable;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class RepeatableAnnotationName {
    @RepeatableAnnotation
    @RepeatableAnnotation
    public String firstName;

    @RepeatableAnnotations({
            @RepeatableAnnotation,
            @RepeatableAnnotation
    })
    public String middleName;

    public RepeatableAnnotationName(final String firstName, final String middleName) {
        this.firstName = firstName;
        this.middleName = middleName;
    }
}
