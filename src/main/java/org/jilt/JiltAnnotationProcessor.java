package org.jilt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import org.jilt.internal.BuilderGeneratorFactory;
import org.jilt.utils.Annotations;

public class JiltAnnotationProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Elements elements;
    private BuilderGeneratorFactory builderGeneratorFactory;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        builderGeneratorFactory = new BuilderGeneratorFactory(filer, elements);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        getAnnotatedElements(roundEnv).forEach((annotatedElement, builderAnnotations) -> {
            try {
                builderGeneratorFactory.forElement(annotatedElement, builderAnnotations).generateBuilderClass();
            } catch(final Exception e) {
                error(annotatedElement, e.getMessage());
            }
        });

        return true;
    }

    private Map<Element, Annotations> getAnnotatedElements(final RoundEnvironment roundEnv) {
        final Set<? extends Element> builderElements = roundEnv.getElementsAnnotatedWith(Builder.class);
        final Map<Element, Annotations> annotatedElements = initMap(builderElements, null, null);
        for(final Element builderElement : builderElements) {
            if(builderElement.getKind() == ElementKind.ANNOTATION_TYPE) {
                annotatedElements.remove(builderElement);
                annotatedElements.putAll(initMap(roundEnv.getElementsAnnotatedWith((TypeElement) builderElement),
                    builderElement.getAnnotation(Builder.class), builderElement.getAnnotation(BuilderInterfaces.class)));
            }
        }

        return annotatedElements;
    }

    private Map<Element, Annotations> initMap(final Set<? extends Element> builderElements, final Builder builderAnnotation,
        final BuilderInterfaces builderInterfaces) {
        final Map<Element, Annotations> map = new HashMap<>();
        for(final Element element : builderElements) {
            map.put(element, new Annotations(builderAnnotation, builderInterfaces));
        }
        return map;
    }

    private void error(final Element element, final String msg, final Object... args) {
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
