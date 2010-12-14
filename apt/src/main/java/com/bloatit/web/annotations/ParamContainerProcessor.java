package com.bloatit.web.annotations;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

    public static final String ROOT = "main/src/main/java/com/bloatit/web/utils/url/";

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        for (TypeElement typeElement : typeElements) {

            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                try {
                    ParamContainer param = element.getAnnotation(ParamContainer.class);
                    String urlName = (param.value().equals("") ? element.getSimpleName().toString() : param.value());

                    JavaGenerator generator;
                    if (param.isComponent()) {
                        generator = new UrlComponentClassGenerator(urlName);
                    } else {
                        generator = new UrlClassGenerator(urlName);
                    }

                    for (Element enclosed : element.getEnclosedElements()) {
                        RequestParam requestParam = enclosed.getAnnotation(RequestParam.class);
                        if (requestParam != null) {
                            String name = enclosed.getSimpleName().toString();
                            String nameString = requestParam.name().equals("") ? enclosed.getSimpleName().toString() : requestParam.name();

                            if (requestParam.generatedFrom().isEmpty()) {
                                generator.addAttribute(enclosed.asType().toString(), name);
                                generator.addGetterSetter(enclosed.asType().toString(), name);
                            } else {
                                generator.addAutoGeneratingGetter(enclosed.asType().toString(), name, requestParam.generatedFrom());
                            }

                            generator.registerAttribute(name,
                                    nameString,
                                    enclosed.asType().toString(),
                                    requestParam.role(),
                                    requestParam.level(),
                                    requestParam.message().value());
                        } else if (enclosed.getAnnotation(PageComponent.class) != null) {
                            generator.addComponentAndGetterSetter(enclosed.getSimpleName().toString());
                            generator.registerComponent(enclosed.getSimpleName().toString());
                        }
                    }

                    FileWriter fstream = new FileWriter(ROOT + generator.getClassName() + ".java");
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(generator.generate());
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
