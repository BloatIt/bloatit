package com.bloatit.framework.webprocessor.annotations.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.lang.model.util.TypeKindVisitor6;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

@SupportedAnnotationTypes("com.bloatit.framework.webprocessor.annotations.ParamContainer")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ParamContainerProcessor extends AbstractProcessor {

    @Override
    public boolean process(final Set<? extends TypeElement> typeElements, final RoundEnvironment env) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotation processing...");

        for (final TypeElement typeElement : typeElements) {
            for (final Element element : env.getElementsAnnotatedWith(typeElement)) {
                try {
                    parseAParamContainer(element);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void parseAParamContainer(final Element element) throws IOException {
        final ParamContainer paramContainer = element.getAnnotation(ParamContainer.class);

        final String urlClassName = element.getSimpleName().toString();
        JavaGenerator generator;
        if (paramContainer.isComponent()) {
            generator = new UrlComponentClassGenerator(urlClassName, "");
        } else {
            generator = new UrlComponentClassGenerator(urlClassName, paramContainer.value());
        }

        generator.setIsAction(isAction(element));


        addParameterForSuperClass(element, generator);

        for (final Element enclosed : element.getEnclosedElements()) {
            parseAnAttribute(generator, enclosed);
        }

        BufferedWriter out = null;
        BufferedWriter outUrl = null;
        try {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "writing " + generator.getClassName());
            final JavaFileObject classFile = this.processingEnv.getFiler().createSourceFile(generator.getClassName());
            out = new BufferedWriter(classFile.openWriter());
            out.write(generator.generateComponentUrlClass());

            if (!paramContainer.isComponent()) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "writing " + generator.getUrlClassName());
                final JavaFileObject urlClassFile = this.processingEnv.getFiler().createSourceFile(generator.getUrlClassName());
                outUrl = new BufferedWriter(urlClassFile.openWriter());
                outUrl.write(generator.generateUrlClass());
            }

        } catch (final Exception e) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
            if (outUrl != null) {
                outUrl.close();
            }
        }
    }

    private boolean isAction(Element element) {



        if(element.getSimpleName().toString().equals("Action")) {
            return true;
        }

        return element.accept(new ElementScanner6<Boolean, Object>() {

            @Override
            public Boolean visitType(TypeElement e, Object p) {
                return isAction(e.getSuperclass());

            }}, false);

    }

    private boolean isAction(TypeMirror type) {

        return type.accept(new TypeVisitor<Boolean, Object>() {

            @Override
            public Boolean visit(TypeMirror t, Object p) {
                return false;
            }

            @Override
            public Boolean visit(TypeMirror t) {
                return false;
            }

            @Override
            public Boolean visitPrimitive(PrimitiveType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitNull(NullType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitArray(ArrayType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitDeclared(DeclaredType t, Object p) {
                return isAction(t.asElement());
            }

            @Override
            public Boolean visitError(ErrorType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitTypeVariable(TypeVariable t, Object p) {
                return false;
            }

            @Override
            public Boolean visitWildcard(WildcardType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitExecutable(ExecutableType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitNoType(NoType t, Object p) {
                return false;
            }

            @Override
            public Boolean visitUnknown(TypeMirror t, Object p) {
                return false;
            }}, null);




    }



    private void addParameterForSuperClass(final Element element, final JavaGenerator generator) {
        final SimpleTypeVisitor6<Element, ProcessingEnvironment> vs = new SimpleTypeVisitor6<Element, ProcessingEnvironment>() {
            @Override
            public Element visitDeclared(final DeclaredType t, final ProcessingEnvironment p) {
                final List<? extends TypeMirror> directSupertypes = p.getTypeUtils().directSupertypes(t);
                if (directSupertypes.size() > 0) {
                    return p.getTypeUtils().asElement(directSupertypes.get(0));
                }
                return null;
            }
        };
        final Element superElement = element.asType().accept(vs, this.processingEnv);
        if (superElement != null && superElement.getAnnotation(ParamContainer.class) != null) {
            generator.setUrlSuperClass(getSecureType(superElement) + "Url");
        }
    }

    private void parseAnAttribute(final JavaGenerator generator, final Element attribute) {

        final RequestParam parm = attribute.getAnnotation(RequestParam.class);
        final Optional optional = attribute.getAnnotation(Optional.class);
        String defaultValue = optional != null ? optional.value() : null;
        if (Optional.DEFAULT_DEFAULT_VALUE.equals(defaultValue)) {
            defaultValue = null;
        }

        // Its a simple param
        if (parm != null) {
            final String attributeName = attribute.getSimpleName().toString();
            final String attributeUrlString = parm.name().isEmpty() ? attribute.getSimpleName().toString() : parm.name();
            final String suggestedValue = RequestParam.DEFAULT_SUGGESTED_VALUE.equals(parm.suggestedValue()) ? null : parm.suggestedValue();

            if (parm.generatedFrom().isEmpty()) {
                generator.addAttribute(getType(attribute), //
                                       getConversionType(attribute), //
                                       attributeUrlString, //
                                       defaultValue, //
                                       suggestedValue, //
                                       attributeName, //
                                       parm.role(), //
                                       parm.conversionErrorMsg().value(), //
                                       attribute.getAnnotation(ParamConstraint.class),
                                       optional != null);
                generator.addGetterSetter(getType(attribute), getConversionType(attribute), attributeName);
                if (optional == null && (parm.role() == Role.GET)) {
                    generator.addConstructorParameter(getType(attribute), attributeName);
                }
                generator.registerAttribute(attributeName);
            } else {
                generator.addAutoGeneratingGetter(getType(attribute), attributeName, parm.generatedFrom());
            }

            // Its not a param but it could be a ParamContainer.
        } else {

            // Find if the type of the attribute has a ParamContainer annotation
            final TypeKindVisitor6<ParamContainer, Integer> vs = new TypeKindVisitor6<ParamContainer, Integer>() {
                @Override
                public ParamContainer visitDeclared(final DeclaredType t, final Integer p) {
                    return t.asElement().getAnnotation(ParamContainer.class);
                }
            };
            final ParamContainer component = attribute.asType().accept(vs, 0);

            if (component != null) {
                generator.addComponentAndGetterSetter(getSecureType(attribute), attribute.getSimpleName().toString());
                generator.registerComponent(attribute.getSimpleName().toString());
            }
        }
    }

    private String getSecureType(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "").replaceAll(".*\\.", "").replace(">", "");
    }

    private String getType(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "");
    }

    private String getConversionType(final Element attribute) {
        String string = attribute.asType().toString().replaceAll("\\<.*\\>", "");
        if (string.endsWith("List")) {
            string = attribute.asType().toString();
            final int start = string.indexOf("<") + 1;
            final int stop = string.lastIndexOf(">");
            return string.substring(start, stop);
        }
        return getType(attribute);
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
    // JMethod jmCreate = jc.method(JMod.PUBLIC | JMod.STATIC,
    // com.somclass.AnyXYZ.class,
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
