package org.jilt.internal;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.lang.reflect.Method;
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
            this.trees = Trees.instance(
                    unwrapJetbrainsProcessingEnv(this.processingEnv));
        }
        return this.trees;
    }

    private static ProcessingEnvironment unwrapJetbrainsProcessingEnv(ProcessingEnvironment wrapped) {
        ProcessingEnvironment unwrapped = null;
        try {
            Class<?> apiWrappers = wrapped.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = (ProcessingEnvironment) unwrapMethod.invoke(null, ProcessingEnvironment.class, wrapped);
        } catch (Exception ignored) {
        }
        return unwrapped == null? wrapped : unwrapped;
    }
}
