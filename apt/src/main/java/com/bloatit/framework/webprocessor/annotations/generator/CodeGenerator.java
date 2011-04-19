package com.bloatit.framework.webprocessor.annotations.generator;

import java.util.Map.Entry;

import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Attribute;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Clazz;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Method;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.MethodCall;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Modifier;

public class CodeGenerator {

    public Clazz generateUrlClass(final UrlDescription desc) {
        final Clazz clazz = new Generator.Clazz(desc.getClassName(), "com.bloatit.web.url");
        clazz.addImport("com.bloatit.framework.utils.parameters.*");
        clazz.addImport("com.bloatit.framework.webprocessor.url.*");

        clazz.addImplements("Cloneable");
        if (desc.getFather() == null) {
            clazz.setExtends("Url");
        } else {
            clazz.setExtends(desc.getFather().getClassName());
        }

        clazz.addAttribute(desc.getComponent().getClassName(), "component");

        final Method staticGetName = clazz.addMethod("String", "getName");
        staticGetName.setStaticFinal("static");
        staticGetName.addLine("return " + desc.getComponent().getComponentNameStr() + ";");

        final Method sharedConstructor = clazz.addConstructor();
        sharedConstructor.addParameter("String", "name");
        sharedConstructor.addParameter("Parameters", "params");
        sharedConstructor.addParameter("SessionParameters", "session");
        sharedConstructor.addLine("super();");
        sharedConstructor.addLine("component = new " + desc.getComponent().getClassName() + "(params, session);");

        final Method constructor = clazz.addConstructor();
        constructor.addParameter("Parameters", "params");
        constructor.addParameter("SessionParameters", "session");
        constructor.addLine("this(getName(), params, session);");

        final Method copyConstructor = clazz.addConstructor();
        copyConstructor.addParameter(clazz.getName(), "other");
        copyConstructor.addLine("super(other);");
        copyConstructor.addLine("component = other.component.clone();");

        final Method generatedConstructor = clazz.addConstructor();
        final MethodCall superMethod = new MethodCall("super");
        for (final ParameterDescription paramDesc : desc.getFathersConstructorParameters()) {
            generatedConstructor.addParameter(paramDesc.getTypeOrTemplateType(), paramDesc.getName());
            superMethod.addParameter(paramDesc.getName());
        }
        final MethodCall componentConstruction = new MethodCall(desc.getComponent().getClassName());
        for (final ParameterDescription paramDesc : desc.getConstructorParameters()) {
            generatedConstructor.addParameter(paramDesc.getTypeOrTemplateType(), paramDesc.getName());
            componentConstruction.addParameter(paramDesc.getName());
        }
        generatedConstructor.addLine(superMethod + ";");
        generatedConstructor.addLine("component = new " + componentConstruction + ";");

        final Method isAction = clazz.addMethod("boolean", "isAction");
        isAction.setOverride();
        isAction.addLine("return " + (desc.isAction() ? "true;" : "false;"));

        final Method getCode = clazz.addMethod("String", "getCode");
        getCode.setOverride();
        getCode.addLine("return getName();");

        final Method doConstructUrl = clazz.addMethod("void", "doConstructUrl");
        doConstructUrl.setOverride();
        doConstructUrl.addParameter("StringBuilder", "sb");
        doConstructUrl.addLine("component.constructUrl(sb);");

        final Method doGetStringParameters = clazz.addMethod("void", "doGetStringParameters");
        doGetStringParameters.setOverride();
        doGetStringParameters.addParameter("Parameters", "parameters");
        doGetStringParameters.addLine("component.getStringParameters(parameters);");

        final Method addParameter = clazz.addMethod("void", "addParameter");
        addParameter.setOverride();
        addParameter.addParameter("String", "key");
        addParameter.addParameter("String", "value");
        addParameter.addLine("component.addParameter(key, value);");

        final Method getMessages = clazz.addMethod("Messages", "getMessages");
        getMessages.setOverride();
        getMessages.addLine("return this.component.getMessages();");

        final Method clone = clazz.addMethod(clazz.getName(), "clone");
        clone.setOverride();
        clone.addLine("return new " + clazz.getName() + "(this);");

        for (final Entry<String, ParameterDescription> entry : desc.getComponent().getParameters().entrySet()) {
            final ParameterDescription param = entry.getValue();

            final String getterName = "get" + Utils.toCamelCase(param.getName(), true);
            final Method getter = clazz.addMethod(param.getTypeWithoutTemplate(), getterName);
            getter.addLine("return this.component." + getterName + "();");

            final String getParameterName = "get" + Utils.toCamelCase(param.getName(), true) + "Parameter";
            final String template = "<" + param.getTypeWithoutTemplate() + ", " + param.getTypeOrTemplateType() + ">";
            final Method getParameter = clazz.addMethod("UrlParameter" + template, getParameterName);
            getParameter.addLine("return this.component." + getParameterName + "();");

            final String setterName = "set" + Utils.toCamelCase(param.getName(), true);
            final Method setter = clazz.addMethod("void", setterName);
            setter.addParameter(param.getTypeWithoutTemplate(), "other");
            setter.addLine("this.component." + setterName + "(other);");
        }

        return clazz;
    }

    public Clazz generateComponentClass(final UrlComponentDescription desc) {
        final Clazz clazz = new Generator.Clazz(desc.getClassName(), "com.bloatit.web.url");
        clazz.setExtends("UrlComponent");

        clazz.addImport("com.bloatit.framework.webprocessor.annotations.RequestParam.Role");
        clazz.addImport("com.bloatit.framework.webprocessor.annotations.RequestParam");
        clazz.addImport("com.bloatit.framework.webprocessor.annotations.ParamConstraint");
        clazz.addImport("com.bloatit.framework.webprocessor.annotations.ConversionErrorException");
        clazz.addImport("com.bloatit.common.Log");
        clazz.addImport("com.bloatit.framework.exceptions.lowlevel.RedirectException");
        clazz.addImport("com.bloatit.framework.utils.*");
        clazz.addImport("com.bloatit.framework.utils.parameters.*");
        clazz.addImport("com.bloatit.framework.webprocessor.url.*");
        clazz.addImport("com.bloatit.framework.webprocessor.url.Loaders.*");

        final Method constructor = clazz.addConstructor();
        constructor.addParameter("Parameters", "params");
        constructor.addParameter("SessionParameters", "session");
        constructor.addLine("this();");
        constructor.addLine("parseSessionParameters(session);");
        constructor.addLine("parseParameters(params);");

        final Method defaultConstructor = clazz.addConstructor();
        defaultConstructor.addLine("super();");
        if (desc.hasUrlParameter()) {
            defaultConstructor.setModifier(Modifier.PRIVATE);

            final Method generatedConstructor = clazz.addConstructor();
            for (final Entry<String, ParameterDescription> entry : desc.getParameters().entrySet()) {
                final ParameterDescription param = entry.getValue();
                if (param.getRealRole() == Role.GET && !param.isOptional()) {
                    generatedConstructor.addParameter(param.getTypeWithoutTemplate(), param.getName());
                    generatedConstructor.addLine("this.set" + Utils.toCamelCase(param.getName(), true) + "(param.getName());");
                }
            }

        }

        final Method doRegister = clazz.addMethod("void", "doRegister");
        doRegister.setOverride();

        final Method clone = clazz.addMethod(clazz.getName(), "clone");
        clone.addLine(clazz.getName() + " other = new " + clazz.getName() + "();");

        for (final Entry<String, ParameterDescription> entry : desc.getParameters().entrySet()) {
            final ParameterDescription param = entry.getValue();
            final String template = "<" + param.getTypeWithoutTemplate() + ", " + param.getTypeOrTemplateType() + ">";
            final Attribute attribute = clazz.addAttribute("UrlParameter" + template,
                                                           param.getName(),
                                                           "get" + Utils.toCamelCase(param.getName(), true) + "Parameter",
                                                           null);

            final MethodCall newParamDescription = new Generator.MethodCall("UrlParameterDescription<" + param.getTypeWithoutTemplate() + ">");
            newParamDescription.addParameter(param.getAttributeNameStr());
            newParamDescription.addParameter(param.getTypeWithoutTemplate() + ".class");
            newParamDescription.addParameter(param.getRole());
            newParamDescription.addParameter(param.getDefaultValueStr());
            newParamDescription.addParameter(param.getSuggestedValueStr());
            newParamDescription.addParameter(param.getConversionErrorMsgStr());
            newParamDescription.addParameter(param.isOptional() ? "true" : "false");
            final MethodCall newParamConstraints = new Generator.MethodCall("UrlParameterConstraints<" + param.getTypeWithoutTemplate() + ">");
            newParamConstraints.addParameter(param.getConstraints().min());
            newParamConstraints.addParameter(param.getConstraints().minIsExclusive() ? "true" : "false");
            newParamConstraints.addParameter(param.getConstraints().max());
            newParamConstraints.addParameter(param.getConstraints().maxIsExclusive() ? "true" : "false");
            newParamConstraints.addParameter(param.isOptional() ? "true" : "false");
            newParamConstraints.addParameter(String.valueOf(param.getConstraints().precision()));
            newParamConstraints.addParameter(String.valueOf(param.getConstraints().length()));
            newParamConstraints.addParameter(Utils.getStr(param.getConstraints().minErrorMsg().value()));
            newParamConstraints.addParameter(Utils.getStr(param.getConstraints().maxErrorMsg().value()));
            newParamConstraints.addParameter(Utils.getStr(param.getConstraints().optionalErrorMsg().value()));
            newParamConstraints.addParameter(Utils.getStr(param.getConstraints().precisionErrorMsg().value()));
            newParamConstraints.addParameter(Utils.getStr(param.getConstraints().LengthErrorMsg().value()));
            final MethodCall newParam = new Generator.MethodCall("UrlParameter" + template);
            if (param.getTypeOrTemplateType().equals(param.getTypeWithoutTemplate())) {
                newParam.addParameter("null");
            } else {
                // TODO change to a collection !
                newParam.addParameter("new ArrayList()");
            }
            newParam.addParameter("new " + newParamDescription);
            newParam.addParameter("new " + newParamConstraints);
            attribute.setStaticEquals("new " + newParam);

            // Value getter
            final Method getter = clazz.addMethod(param.getTypeWithoutTemplate(), "get" + Utils.toCamelCase(param.getName(), true));
            getter.addLine("return this." + param.getName() + ".getValue();");

            // Value Setter
            final Method setter = clazz.addMethod("void", "set" + Utils.toCamelCase(param.getName(), true));
            setter.addParameter(param.getTypeWithoutTemplate(), "other");
            setter.addLine("this." + param.getName() + ".setValue(other);");

            doRegister.addLine("register(" + param.getName() + ");");

            clone.addLine("other." + param.getName() + " = this." + param.getName() + ".clone();");
            clone.addLine("return other;");
        }

        return clazz;
    }

}
