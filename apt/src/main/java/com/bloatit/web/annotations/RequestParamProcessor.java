package com.bloatit.web.annotations;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SuppressWarnings("restriction")
@SupportedAnnotationTypes("com.bloatit.web.utils.annotations.RequestParam")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class RequestParamProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        for (TypeElement typeElement : typeElements) {
            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                System.out.println(element.getSimpleName());
            }
        }
        return false;
    }
}
