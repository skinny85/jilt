package org.jilt;

import com.sun.source.util.Trees;
import org.jilt.internal.BuilderGeneratorFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Set;

public class JiltAnnotationProcessor extends AbstractProcessor {
    private Messager messager;
    private BuilderGeneratorFactory builderGeneratorFactory;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.messager = processingEnv.getMessager();
        this.builderGeneratorFactory = new BuilderGeneratorFactory(
                processingEnv.getFiler(), processingEnv.getElementUtils(),
                Trees.instance(processingEnv));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            try {
                builderGeneratorFactory.forElement(annotatedElement, roundEnv).generateBuilderClass();
            } catch (Exception e) {
                error(annotatedElement, e.getMessage());
                return true;
            }
        }

        return true;
    }

    private void error(Element element, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), element);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Builder.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
