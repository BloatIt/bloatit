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

class ParameterDescription extends Description implements Comparable<ParameterDescription> {

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributeName == null) ? 0 : attributeName.hashCode());
        result = prime * result + ((conversionErrorMsg == null) ? 0 : conversionErrorMsg.hashCode());
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + ((generateFrom == null) ? 0 : generateFrom.hashCode());
        result = prime * result + (isOptional ? 1231 : 1237);
        result = prime * result + ((lengthConstraint == null) ? 0 : lengthConstraint.hashCode());
        result = prime * result + ((maxConstraint == null) ? 0 : maxConstraint.hashCode());
        result = prime * result + ((minConstraint == null) ? 0 : minConstraint.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((nonOptional == null) ? 0 : nonOptional.hashCode());
        result = prime * result + ((precisionConstraint == null) ? 0 : precisionConstraint.hashCode());
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        result = prime * result + ((suggestedValue == null) ? 0 : suggestedValue.hashCode());
        result = prime * result + ((typeOrTemplateType == null) ? 0 : typeOrTemplateType.hashCode());
        result = prime * result + ((typeWithoutTemplate == null) ? 0 : typeWithoutTemplate.hashCode());
        result = prime * result + ((typeWithoutTemplateSimple == null) ? 0 : typeWithoutTemplateSimple.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ParameterDescription other = (ParameterDescription) obj;
        if (attributeName == null) {
            if (other.attributeName != null)
                return false;
        } else if (!attributeName.equals(other.attributeName))
            return false;
        if (conversionErrorMsg == null) {
            if (other.conversionErrorMsg != null)
                return false;
        } else if (!conversionErrorMsg.equals(other.conversionErrorMsg))
            return false;
        if (defaultValue == null) {
            if (other.defaultValue != null)
                return false;
        } else if (!defaultValue.equals(other.defaultValue))
            return false;
        if (generateFrom == null) {
            if (other.generateFrom != null)
                return false;
        } else if (!generateFrom.equals(other.generateFrom))
            return false;
        if (isOptional != other.isOptional)
            return false;
        if (lengthConstraint == null) {
            if (other.lengthConstraint != null)
                return false;
        } else if (!lengthConstraint.equals(other.lengthConstraint))
            return false;
        if (maxConstraint == null) {
            if (other.maxConstraint != null)
                return false;
        } else if (!maxConstraint.equals(other.maxConstraint))
            return false;
        if (minConstraint == null) {
            if (other.minConstraint != null)
                return false;
        } else if (!minConstraint.equals(other.minConstraint))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (nonOptional == null) {
            if (other.nonOptional != null)
                return false;
        } else if (!nonOptional.equals(other.nonOptional))
            return false;
        if (precisionConstraint == null) {
            if (other.precisionConstraint != null)
                return false;
        } else if (!precisionConstraint.equals(other.precisionConstraint))
            return false;
        if (role != other.role)
            return false;
        if (suggestedValue == null) {
            if (other.suggestedValue != null)
                return false;
        } else if (!suggestedValue.equals(other.suggestedValue))
            return false;
        if (typeOrTemplateType == null) {
            if (other.typeOrTemplateType != null)
                return false;
        } else if (!typeOrTemplateType.equals(other.typeOrTemplateType))
            return false;
        if (typeWithoutTemplate == null) {
            if (other.typeWithoutTemplate != null)
                return false;
        } else if (!typeWithoutTemplate.equals(other.typeWithoutTemplate))
            return false;
        if (typeWithoutTemplateSimple == null) {
            if (other.typeWithoutTemplateSimple != null)
                return false;
        } else if (!typeWithoutTemplateSimple.equals(other.typeWithoutTemplateSimple))
            return false;
        return true;
    }

    @Override
    public int compareTo(ParameterDescription o) {
        return name.compareTo(o.name);
    }
}
