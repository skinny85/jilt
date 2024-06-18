package org.jilt.test.data.annotations;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER)
public class RepeatableAnnotationsName {
    @RepeatableAnnotation
    @RepeatableAnnotation
    public String firstName;

    @RepeatableAnnotations({@RepeatableAnnotation, @RepeatableAnnotation})
    public String lastName;

    public RepeatableAnnotationsName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
