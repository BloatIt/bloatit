package com.bloatit.framework.webprocessor.annotations.generator;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.LengthConstraint;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.PrecisionConstraint;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

class ParameterDescription extends Description {

    // Name of the parameter.
    private final String attributeName;

    // Values for creating the parameters.
    private final String name;
    private final String typeOrTemplateType;
    private final String typeWithoutTemplateSimple;
    private final String typeWithoutTemplate;
    private final Role role;
    private final String defaultValue;
    private final String suggestedValue;
    private final String conversionErrorMsg;
    private final String generateFrom;
    private final boolean isOptional;
    private final NonOptional nonOptional;
    private final LengthConstraint lengthConstraint;
    private final MaxConstraint maxConstraint;
    private final MinConstraint minConstraint;
    private final PrecisionConstraint precisionConstraint;

    public ParameterDescription(final Element element,
                                final RequestParam container,
                                final Optional optional,
                                final NonOptional nonOptional,
                                final LengthConstraint lengthConstraint,
                                final MaxConstraint maxConstraint,
                                final MinConstraint minConstraint,
                                final PrecisionConstraint precisionConstraint) {
        this.nonOptional = nonOptional;
        this.lengthConstraint = lengthConstraint;
        this.maxConstraint = maxConstraint;
        this.minConstraint = minConstraint;
        this.precisionConstraint = precisionConstraint;
        attributeName = element.getSimpleName().toString();

        name = computeName(container.name(), element.getSimpleName().toString(), container.role(), Utils.getDeclaredName(element));
        typeOrTemplateType = getTypeOrTemplate(element);
        typeWithoutTemplate = getTypeWithoutTemplate(element);
        typeWithoutTemplateSimple = getTypeWithoutTemplateSimple(element);
        role = container.role();
        suggestedValue = container.suggestedValue().equals(RequestParam.DEFAULT_SUGGESTED_VALUE) ? null : container.suggestedValue();
        conversionErrorMsg = container.message().value();
        if (!container.generatedFrom().isEmpty()) {
            generateFrom = container.generatedFrom();
        } else {
            generateFrom = null;
        }
        if (optional != null) {
            isOptional = true;
            defaultValue = optional.value().equals(Optional.DEFAULT_DEFAULT_VALUE) ? null : optional.value();
        } else {
            isOptional = false;
            defaultValue = null;
        }
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

    private String getTypeWithoutTemplate(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "");
    }

    private String getTypeOrTemplate(final Element attribute) {
        String string = attribute.asType().toString().replaceAll("\\<.*\\>", "");
        if (string.endsWith("List")) {
            string = attribute.asType().toString();
            final int start = string.indexOf("<") + 1;
            final int stop = string.lastIndexOf(">");
            return string.substring(start, stop);
        }
        return getTypeWithoutTemplate(attribute);
    }

    private String getTypeWithoutTemplateSimple(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "").replaceAll(".*\\.", "").replace(">", "");
    }

    public String getTypeWithoutTemplate() {
        return typeWithoutTemplate;
    }

    public final String getTypeOrTemplateType() {
        return typeOrTemplateType;
    }

    public final String getAttributeName() {
        return attributeName;
    }

    public final String getNameStr() {
        return Utils.getStr(name);
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

    public String getGenerateFrom() {
        return generateFrom;
    }

    public NonOptional getNonOptional() {
        return nonOptional;
    }

    public LengthConstraint getLengthConstraint() {
        return lengthConstraint;
    }

    public MaxConstraint getMaxConstraint() {
        return maxConstraint;
    }

    public MinConstraint getMinConstraint() {
        return minConstraint;
    }

    public PrecisionConstraint getPrecisionConstraint() {
        return precisionConstraint;
    }
}
