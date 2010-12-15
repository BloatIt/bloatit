package com.bloatit.web.annotations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.TypeKindVisitor6;

@SuppressWarnings("restriction")
@SupportedAnnotationTypes("com.bloatit.web.annotations.ParamContainer")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParamContainerProcessor extends AbstractProcessor {

    public static final String ROOT = "main/src/main/java/com/bloatit/web/utils/url/";

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        System.out.println("Launch the Custom APT !");
        for (TypeElement typeElement : typeElements) {

            for (Element element : env.getElementsAnnotatedWith(typeElement)) {
                try {
                    parseAParamContainer(element);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void parseAParamContainer(Element element) throws IOException {
        ParamContainer paramContainer = element.getAnnotation(ParamContainer.class);

        String urlClassName = (paramContainer.value().equals("") ? element.getSimpleName().toString() : paramContainer.value());
        JavaGenerator generator;
        if (paramContainer.isComponent()) {
            generator = new UrlComponentClassGenerator(urlClassName);
        } else {
            generator = new UrlClassGenerator(urlClassName);
        }

        System.out.println("    generating " + generator.getClassName());
        for (Element enclosed : element.getEnclosedElements()) {
            parseAnAttribute(generator, enclosed);
        }

        FileWriter fstream = new FileWriter(ROOT + generator.getClassName() + ".java");
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(generator.generate());
        out.close();
    }

    private void parseAnAttribute(JavaGenerator generator, Element attribute) {

        RequestParam parm = attribute.getAnnotation(RequestParam.class);

        // Its a simple param
        if (parm != null) {
            String attributeName = attribute.getSimpleName().toString();
            String attributeUrlString = parm.name().isEmpty() ? attribute.getSimpleName().toString() : parm.name();

            if (parm.generatedFrom().isEmpty()) {
                generator.addAttribute(attribute.asType().toString(), attributeName);
                generator.addGetterSetter(attribute.asType().toString(), attributeName);
            } else {
                generator.addAutoGeneratingGetter(attribute.asType().toString(), attributeName, parm.generatedFrom());
            }

            generator.registerAttribute(attributeName, attributeUrlString, attribute.asType().toString(), parm.role(), parm.level(), parm.message()
                    .value());

            // Its not a param but it could be a ParamContainer.
        } else {

            // Find if the type of the attribute has a ParamContainer annotation
            TypeKindVisitor6<ParamContainer, Integer> vs = new TypeKindVisitor6<ParamContainer, Integer>() {
                @Override
                public ParamContainer visitDeclared(DeclaredType t, Integer p) {
                    return t.asElement().getAnnotation(ParamContainer.class);
                }
            };
            ParamContainer component = attribute.asType().accept(vs, 0);

            if (component != null) {
                generator.addComponentAndGetterSetter(component.value(), attribute.getSimpleName().toString());
                generator.registerComponent(attribute.getSimpleName().toString());
            }
        }
    }
}
