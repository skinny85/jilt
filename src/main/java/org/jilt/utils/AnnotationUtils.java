package org.jilt.utils;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Set;

public final class AnnotationUtils {
    private static final Set<ElementType> NON_PARAMETER_ANNOTATIONS = Set.of(ElementType.TYPE, ElementType.FIELD,
            ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE,
            ElementType.PACKAGE, ElementType.TYPE_PARAMETER, ElementType.MODULE, ElementType.RECORD_COMPONENT);

    private AnnotationUtils() {
    }

    public static boolean isAnnotationAllowedInParameter(AnnotationMirror annotation) {
        Target targetAnnotation = annotation.getAnnotationType().asElement().getAnnotation(Target.class);
        if (targetAnnotation == null) {
            return true;
        }

        for (ElementType elementType : targetAnnotation.value()) {
            if (NON_PARAMETER_ANNOTATIONS.contains(elementType)) {
                return false;
            }
        }

        return true;
    }
}
