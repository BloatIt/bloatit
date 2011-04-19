package com.bloatit.framework.webprocessor.annotations.generator;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

class ParameterDescription {
    // Name of the parameter.
    private final String name;

    // Values for creating the parameters.
    private final String attributeName;
    private final String typeOrTemplateType;
    private final String typeWithoutTemplateSimple;
    private final String typeWithoutTemplate;
    private final Role role;
    private final String defaultValue;
    private final String suggestedValue;
    private final String conversionErrorMsg;
    private final boolean isOptional;
    private final ParamConstraint constraints;

    public ParameterDescription(final Element element, final RequestParam container, final ParamConstraint constraints, final Optional optional) {
        typeOrTemplateType = getType(element);
        name = computeName(container.name(), element.getSimpleName().toString(), container.role(), Utils.getDeclaredName(element));

        typeWithoutTemplate = getSecureType(element);
        attributeName = element.getSimpleName().toString();
        typeWithoutTemplateSimple = getConversionType(element);
        role = container.role();
        suggestedValue = container.suggestedValue();
        conversionErrorMsg = container.conversionErrorMsg().value();
        if (optional != null) {
            isOptional = true;
            defaultValue = optional.value();
        } else {
            isOptional = false;
            defaultValue = null;
        }
        this.constraints = constraints;
    }

    private String computeName(final String annotationName, final String javaName, final Role role, final String className) {
        if (annotationName.isEmpty()) {
            if (role == Role.POST || role == Role.SESSION) {
                return className.toLowerCase() + "_" + javaName;
            }
            return javaName;
        }
        return annotationName;
    }

    private String getSecureType(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "");
    }

    private String getConversionType(final Element attribute) {
        String string = attribute.asType().toString().replaceAll("\\<.*\\>", "");

        // TODO use inherit from collection
        if (string.endsWith("List")) {
            string = attribute.asType().toString();
            final int start = string.indexOf("<") + 1;
            final int stop = string.lastIndexOf(">");
            return string.substring(start, stop);
        }
        return getType(attribute);
    }

    private String getType(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "").replaceAll(".*\\.", "").replace(">", "");
    }

    public String getTypeWithoutTemplate() {
        return typeWithoutTemplate;
    }

    public final String getTypeOrTemplateType() {
        return typeOrTemplateType;
    }

    public final String getName() {
        return name;
    }

    public final String getAttributeNameStr() {
        return Utils.getStr(attributeName);
    }

    public final String getTypeWithoutTemplateSimple() {
        return typeWithoutTemplateSimple;
    }

    public final String getRole() {
        return "Role." + role.name();
    }
    public final Role getRealRole() {
        return role;
    }

    public final String getDefaultValueStr() {
        return Utils.getStr(defaultValue);
    }

    public final String getSuggestedValueStr() {
        if (suggestedValue.equals(RequestParam.DEFAULT_SUGGESTED_VALUE)) {
            return "RequestParam.DEFAULT_SUGGESTED_VALUE";
        }
        return Utils.getStr(suggestedValue);
    }

    public final String getConversionErrorMsgStr() {
        if (conversionErrorMsg.equals(RequestParam.DEFAULT_ERROR_MSG)) {
            return "RequestParam.DEFAULT_ERROR_MSG";
        }
        return Utils.getStr(conversionErrorMsg);
    }

    public final boolean isOptional() {
        return isOptional;
    }

    public final ParamConstraint getConstraints() {
        return constraints;
    }

}
