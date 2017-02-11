package org.jilt.internal;

import javax.lang.model.element.Element;

public abstract class AbstractBuilderGenerator {
    public abstract void generateBuilderClass(Element annotatedElement) throws Exception;
}
