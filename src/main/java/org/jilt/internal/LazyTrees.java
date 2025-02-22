package org.jilt.internal;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;

public final class LazyTrees {
    private final ProcessingEnvironment processingEnv;
    private Trees trees;

    public LazyTrees(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
    }

    public TreePath getPath(VariableElement attribute) {
        return this.getTrees().getPath(attribute);
    }

    private Trees getTrees() {
        if (this.trees == null) {
            this.trees = Trees.instance(this.processingEnv);
        }
        return this.trees;
    }
}
