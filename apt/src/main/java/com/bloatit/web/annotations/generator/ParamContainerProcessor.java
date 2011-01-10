package com.bloatit.web.annotations.generator;

import java.io.BufferedWriter;
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
import javax.lang.model.util.Elements;
import javax.lang.model.util.TypeKindVisitor6;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;

@SupportedAnnotationTypes("com.bloatit.web.annotations.ParamContainer")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParamContainerProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> typeElements, RoundEnvironment env) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotation processing...");

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
            generator = new UrlClassGenerator(urlClassName, paramContainer.value(), element.asType().toString());
        }

        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "generating " + generator.getClassName());
        for (Element enclosed : element.getEnclosedElements()) {
            parseAnAttribute(generator, enclosed);
        }

        BufferedWriter out = null;
        Elements elementUtils = this.processingEnv.getElementUtils();
        try {
            JavaFileObject classFile = this.processingEnv.getFiler()
                    .createSourceFile(generator.getClassName(), elementUtils.getTypeElement("coucou"));
            out = new BufferedWriter(classFile.openWriter());
            out.write(generator.generate());
        } catch (Exception e) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private void parseAnAttribute(JavaGenerator generator, Element attribute) {

        RequestParam parm = attribute.getAnnotation(RequestParam.class);

        // Its a simple param
        if (parm != null) {
            String attributeName = attribute.getSimpleName().toString();
            String attributeUrlString = parm.name().isEmpty() ? attribute.getSimpleName().toString() : parm.name();

            if (parm.generatedFrom().isEmpty()) {
                generator.addAttribute(getType(attribute), attributeUrlString, parm.defaultValue(), attributeName, parm.role(), parm.level(), parm
                        .message().value());
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

    // try {
    //
    // /* Creating java code model classes */
    // JCodeModel jCodeModel = new JCodeModel();
    //
    // /* Adding packages here */
    // JPackage jp = jCodeModel._package(factroyPackage);
    //
    // /* Giving Class Name to Generate */
    // JDefinedClass jc = jp._class("GeneratedFactory");
    //
    // /* Adding annotation for the Class */
    // jc.annotate(com.myannotation.AnyXYZ.class);
    //
    // /* Adding class level coment */
    // JDocComment jDocComment = jc.javadoc();
    // jDocComment.add("Class Level Java Docs");
    //
    //
    // /* Adding method to the Class which is public static and returns
    // com.somclass.AnyXYZ.class */
    // String mehtodName = "myFirstMehtod";
    // JMethod jmCreate = jc.method(JMod.PUBLIC | JMod.STATIC, com.somclass.AnyXYZ.class,
    // "create" + mehtodName);
    //
    // /* Addign java doc for method */
    // jmCreate.javadoc().add("Method Level Java Docs");
    //
    // /* Adding method body */
    // JBlock jBlock = jmCreate.body();
    //
    // /* Defining method parameter */
    // JType jt = getTypeDetailsForCodeModel(jCodeModel, "Unsigned32");
    // if (jt != null) {
    // jmCreate.param(jt, "data");
    // } else {
    // jmCreate.param(java.lang.String.class, "data");
    // }
    //
    // /* Defining some class Variable in mthod body */
    // JClass jClassavpImpl = jCodeModel.ref(com.somclass.AnyXYZ.class);
    // jvarAvpImpl = jBlock.decl(jClassavpImpl, "varName");
    // jvarAvpImpl.init(JExpr._new(jClassavpImpl));
    //
    //
    // /* Adding some direct statement */
    // jBlock.directStatement("varName.setCode(100);");
    //
    // /* returning varibalbe */
    // jBlock._return(jvarAvpImpl);
    //
    // /* Building class at given location */
    // jCodeModel.build(new File("generated/src"));
    //
    // } catch (JAXBException ex) {
    // logger.log(Level.SEVERE, "JAXBException:" + ex);
    // ex.printStackTrace();
    // } catch (Exception ex) {
    // logger.log(Level.SEVERE, "Other Exception which in not catched:" + ex);
    // ex.printStackTrace();
    // }
    // }

}
