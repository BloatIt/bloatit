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

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

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

        String urlClassName = element.getSimpleName().toString();
        JavaGenerator generator;
        if (paramContainer.isComponent()) {
            generator = new UrlComponentClassGenerator(urlClassName);
        } else {
            generator = new UrlClassGenerator(urlClassName, element.asType().toString());
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
            // String attributeUrlString = parm.name().isEmpty() ? attribute.getSimpleName().toString() : parm.name();

            if (parm.generatedFrom().isEmpty()) {
                generator.addAttribute(getType(attribute), attributeName, parm.role(), parm.level(), parm.message().value());
                generator.addGetterSetter(getType(attribute), attributeName);
                if (!parm.defaultValue().equals(RequestParam.defaultDefaultValue)) {
                    generator.addDefaultParameter(attributeName, getType(attribute), parm.defaultValue());
                } else if (parm.level() == Level.ERROR && (parm.role() == Role.GET || parm.role() == Role.PRETTY)) {
                    generator.addConstructorParameter(getType(attribute), attributeName);
                }
                generator.registerAttribute(attributeName);
            } else {
                generator.addAutoGeneratingGetter(getType(attribute), attributeName, parm.generatedFrom());
            }


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
                generator.addComponentAndGetterSetter(getSecureType(attribute), attribute.getSimpleName().toString());
                System.out.println(getType(attribute) + " " + getSecureType(attribute));
                generator.registerComponent(attribute.getSimpleName().toString());
            }
        }
    }

    private String getSecureType(Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "").replaceAll(".*\\.", "").replace(">", "");
    }

    private String getType(Element attribute) {
        return attribute.asType().toString();
    }
}
