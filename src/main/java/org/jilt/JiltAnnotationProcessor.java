package org.jilt;

import org.jilt.internal.BuilderGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.Set;

public class JiltAnnotationProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Elements elements;
    private BuilderGenerator builderGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        builderGenerator = new BuilderGenerator(elements, filer);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            try {
                builderGenerator.generateBuilderClass(annotatedElement);
            } catch (Exception e) {
                return error(annotatedElement, e.getMessage());
            }
        }

        return true;
    }

    private boolean error(Element element, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), element);
        return true;
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
