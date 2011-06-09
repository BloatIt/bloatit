package com.bloatit.framework.webprocessor.annotations.generator;

import com.bloatit.framework.webprocessor.annotations.generator.Generator.Attribute;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Clazz;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.Method;
import com.bloatit.framework.webprocessor.annotations.generator.Generator.MethodCall;

public class CodeGenerator {

    public Clazz generateUrlClass(final UrlDescription desc) {
        final Clazz clazz = new Generator.Clazz(desc.getClassName(), "com.bloatit.web.url");
        clazz.addImport("com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol");
        clazz.addImport("com.bloatit.framework.utils.parameters.*");
        clazz.addImport("com.bloatit.framework.webprocessor.url.*");

        clazz.addImplements("Cloneable");
        if (desc.getFather() == null) {
            clazz.setExtends("Url");
        } else {
            clazz.setExtends(desc.getFather().getClassName());
        }

        clazz.addAttribute(desc.getComponent().getClassName(), "component");

        final Method staticGetName = clazz.addMethod("String", "getPageName");
        staticGetName.setStaticFinal("static");
        staticGetName.addLine("return " + desc.getComponent().getCodeNameStr() + ";");

        final Method constructor = clazz.addConstructor();
        constructor.addParameter("Parameters", "params");
        constructor.addParameter("SessionParameters", "session");
        if (desc.getFather() != null) {
            constructor.addLine("super(params, session);");
        }
        constructor.addLine("this.component = new " + desc.getComponent().getClassName() + "(params, session);");

        final Method copyConstructor = clazz.addConstructor();
        copyConstructor.addParameter(clazz.getName(), "other");
        copyConstructor.addLine("super(other);");
        copyConstructor.addLine("component = other.component.clone();");

        final Method generatedConstructor = clazz.addConstructor();
        final MethodCall superMethod = new MethodCall("super");
        for (final ParameterDescription paramDesc : desc.getFathersConstructorParameters()) {
            generatedConstructor.addParameter(paramDesc.getTypeOrTemplateType(), paramDesc.getAttributeName());
            superMethod.addParameter(paramDesc.getAttributeName());
        }
        final MethodCall componentConstruction = new MethodCall(desc.getComponent().getClassName());
        for (final ParameterDescription paramDesc : desc.getConstructorParameters()) {
            generatedConstructor.addParameter(paramDesc.getTypeOrTemplateType(), paramDesc.getAttributeName());
            componentConstruction.addParameter(paramDesc.getAttributeName());
        }
        generatedConstructor.addLine(superMethod + ";");
        generatedConstructor.addLine("component = new " + componentConstruction + ";");

        if (desc.getFather() == null) {
            final Method isAction = clazz.addMethod("boolean", "isAction");
            isAction.setOverride();
            isAction.addLine("return " + (desc.isAction() ? "true;" : "false;"));
        }

        final Method getProtocol = clazz.addMethod("Protocol", "getProtocol");
        getProtocol.setOverride();
        getProtocol.addLine("return Protocol." + desc.getComponent().getProtocol() + ";");

        final Method getCode = clazz.addMethod("String", "getCode");
        getCode.setOverride();
        getCode.addLine("return getPageName();");

        final Method doConstructUrl = clazz.addMethod("void", "doConstructUrl");
        doConstructUrl.setOverride();
        doConstructUrl.addParameter("StringBuilder", "sb");
        if (desc.getFather() != null) {
            doConstructUrl.addLine("super.doConstructUrl(sb);");
        }
        doConstructUrl.addLine("component.constructUrl(sb);");

        final Method doGetStringParameters = clazz.addMethod("void", "doGetParametersAsStrings");
        doGetStringParameters.setOverride();
        doGetStringParameters.addParameter("Parameters", "parameters");
        if (desc.getFather() != null) {
            doGetStringParameters.addLine("super.doGetParametersAsStrings(parameters);");
        }
        doGetStringParameters.addLine("component.getParametersAsStrings(parameters);");

        final Method addParameter = clazz.addMethod("void", "addParameter");
        addParameter.setOverride();
        addParameter.addParameter("String", "key");
        addParameter.addParameter("String", "value");
        if (desc.getFather() != null) {
            addParameter.addLine("super.addParameter(key, value);");
        }
        addParameter.addLine("component.addParameter(key, value);");

        final Method getMessages = clazz.addMethod("Messages", "getMessages");
        getMessages.setOverride();
        if (desc.getFather() != null) {
            getMessages.addLine("final Messages messages = super.getMessages();");
            getMessages.addLine("messages.addAll(this.component.getMessages());");
            getMessages.addLine("return messages;");
        } else {
            getMessages.addLine("return this.component.getMessages();");
        }

        final Method clone = clazz.addMethod(clazz.getName(), "clone");
        clone.setOverride();
        clone.addLine("return new " + clazz.getName() + "(this);");

        for (final ParameterDescription param : desc.getComponent().getParameters()) {
            final String getterName = "get" + Utils.firstCharUpper(param.getAttributeName());
            final Method getter = clazz.addMethod(param.getTypeWithoutTemplate(), getterName);
            getter.addLine("return this.component." + getterName + "();");

            final String getParameterName = "get" + Utils.firstCharUpper(param.getAttributeName()) + "Parameter";
            final String template = "<" + param.getTypeWithoutTemplate() + ", " + param.getTypeOrTemplateType() + ">";
            final Method getParameter = clazz.addMethod("UrlParameter" + template, getParameterName);
            getParameter.addLine("return this.component." + getParameterName + "();");

            final String setterName = "set" + Utils.firstCharUpper(param.getAttributeName());
            final Method setter = clazz.addMethod("void", setterName);
            setter.addParameter(param.getTypeWithoutTemplate(), "other");
            setter.addLine("this.component." + setterName + "(other);");
        }

        for (final ComponentDescription subComponent : desc.getComponent().getSubComponents()) {
            final String getParameterName = "get" + Utils.firstCharUpper(subComponent.getAttributeName()) + "Url";
            final Method getParameter = clazz.addMethod(subComponent.getClassName(), getParameterName);
            getParameter.addLine("return this.component." + getParameterName + "();");

            final String setterName = "set" + Utils.firstCharUpper(subComponent.getAttributeName()) + "Url";
            final Method setter = clazz.addMethod("void", setterName);
            setter.addParameter(subComponent.getClassName(), "other");
            setter.addLine("this.component." + setterName + "(other);");
        }

        return clazz;
    }

    public Clazz generateComponentClass(final ComponentDescription desc) {
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
        clazz.addImport("java.util.ArrayList");

        final Method constructor = clazz.addConstructor();
        constructor.addParameter("Parameters", "params");
        constructor.addParameter("SessionParameters", "session");

        final Method generatedConstructor = clazz.addConstructor();
        for (final ParameterDescription param : desc.getUrlParameters()) {
            generatedConstructor.addParameter(param.getTypeWithoutTemplate(), param.getAttributeName());
            generatedConstructor.addLine("this.set" + Utils.firstCharUpper(param.getAttributeName()) + "(" + param.getAttributeName() + ");");
        }

        final Method doRegister = clazz.addMethod("void", "doRegister");
        doRegister.setOverride();

        final Method clone = clazz.addMethod(clazz.getName(), "clone");
        clone.setOverride();
        clone.addLine(clazz.getName() + " other = new " + clazz.getName() + "((Parameters) null, (SessionParameters) null);");

        for (final ParameterDescription param : desc.getParameters()) {
            final String template = "<" + param.getTypeWithoutTemplate() + ", " + param.getTypeOrTemplateType() + ">";

            // Attribute
            final Attribute attribute = clazz.addAttribute("UrlParameter" + template, param.getAttributeName());

            // Getter
            final Method getter = clazz.addMethod("UrlParameter" + template, "get" + Utils.firstCharUpper(param.getAttributeName()) + "Parameter");
            getter.addLine("return this." + param.getAttributeName() + ";");

            // Static equals ( = new UrlParameter ...)
            final MethodCall newParamDescription = new Generator.MethodCall("UrlParameterDescription<" + param.getTypeOrTemplateType() + ">");
            newParamDescription.addParameter(param.getNameStr());
            newParamDescription.addParameter(param.getTypeOrTemplateType() + ".class");
            newParamDescription.addParameter(param.getRole());
            newParamDescription.addParameter(param.getDefaultValueStr());
            newParamDescription.addParameter(param.getSuggestedValueStr());
            newParamDescription.addParameter(param.getConversionErrorMsgStr());
            newParamDescription.addParameter(param.isOptional() ? "true" : "false");
            final MethodCall newParam = new Generator.MethodCall("UrlParameter" + template);
            if (param.getTypeOrTemplateType().equals(param.getTypeWithoutTemplate())) {
                newParam.addParameter("null");
            } else {
                newParam.addParameter("new ArrayList()");
            }
            newParam.addParameter("new " + newParamDescription);
            attribute.setStaticEquals("new " + newParam);

            // Value getter
            final Method valueGetter = clazz.addMethod(param.getTypeWithoutTemplate(), "get" + Utils.firstCharUpper(param.getAttributeName()));
            valueGetter.addLine("return this." + param.getAttributeName() + ".getValue();");

            // Value Setter
            final Method setter = clazz.addMethod("void", "set" + Utils.firstCharUpper(param.getAttributeName()));
            setter.addParameter(param.getTypeWithoutTemplate(), "other");
            setter.addLine("this." + param.getAttributeName() + ".setValue(other);");
            for (final ParameterDescription generatedParam : desc.getParameterGeneratedFromMe(param)) {
                setter.addLine("try {");
                setter.addLine("    " + generatedParam.getAttributeName() + ".setValue(" + param.getAttributeName() + ".getValue().get"
                        + Utils.firstCharUpper(generatedParam.getAttributeName()) + "());");
                setter.addLine("} catch (final Exception e) {");
                setter.addLine("    Log.framework().warn(\"Error in pretty value generation.\", e);");
                setter.addLine("}");
            }

            doRegister.addLine("register(" + param.getAttributeName() + ");");

            clone.addLine("other." + param.getAttributeName() + " = this." + param.getAttributeName() + ".clone();");
        }

        for (final ComponentDescription subComponent : desc.getSubComponents()) {
            final String subComponentName = Utils.firstCharLower(subComponent.getClassName());

            // Add an attribute
            clazz.addAttribute(subComponent.getClassName(), subComponentName, //
                               "get" + Utils.firstCharUpper(subComponent.getAttributeName()) + "Url", //
                               "set" + Utils.firstCharUpper(subComponent.getAttributeName()) + "Url");

            // register it
            doRegister.addLine("register(" + subComponentName + ");");

            // add it to the clone method
            clone.addLine("other." + subComponentName + " = this." + subComponentName + ".clone();");

            // Construct it
            constructor.addLine("this." + subComponentName + " = new " + subComponent.getClassName() + "(params, session);");

            final MethodCall subcomponentConstruction = new MethodCall(subComponent.getClassName());
            for (final ParameterDescription subParameters : subComponent.getAllUrlParameters()) {
                final String parameterName = subParameters.getAttributeName() + subComponent.getClassName();
                generatedConstructor.addParameter(subParameters.getTypeWithoutTemplate(), parameterName);
                subcomponentConstruction.addParameter(parameterName);
            }
            generatedConstructor.addLine("this." + subComponentName + " = new " + subcomponentConstruction + ";");
        }

        constructor.addLine("parseSessionParameters(session);");
        constructor.addLine("parseParameters(params);");

        clone.addLine("return other;");

        return clazz;
    }

}
