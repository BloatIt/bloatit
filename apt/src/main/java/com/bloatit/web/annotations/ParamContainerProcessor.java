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
@SupportedAnnotationTypes("com.bloatit.web.annotations.ParamContainer")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParamContainerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        System.out.println("PLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP");
        for (TypeElement typeElement : typeElements) {
            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                ParamContainer param = element.getAnnotation(ParamContainer.class);
                String urlName = (param.value().equals("") ? element.getSimpleName().toString() : param.value());
                UrlClassGenerator urlClassGenerator = new UrlClassGenerator(urlName);

                for (Element enclosed : element.getEnclosedElements()) {
                    RequestParam requestParam = enclosed.getAnnotation(RequestParam.class);
                    if (requestParam != null) {
                        String name = requestParam.name().equals("") ? enclosed.getSimpleName().toString() : requestParam.name();
                        urlClassGenerator.addAttribute(enclosed.asType().toString(), name);
                        urlClassGenerator.registerAttribute(name, requestParam.role(), requestParam.level(), requestParam.message().value());
                    } else if (enclosed.getAnnotation(PageComponent.class) != null) {
                         urlClassGenerator.addComponent(enclosed.getSimpleName().toString());
                         urlClassGenerator.registerComponent(enclosed.getSimpleName().toString());
                    }

                }
                System.out.println(urlClassGenerator.generate());
            }
        }
        return false;
    }
}
