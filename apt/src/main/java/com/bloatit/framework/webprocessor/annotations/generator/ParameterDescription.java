package com.bloatit.framework.webprocessor.annotations.generator;

import javax.lang.model.element.Element;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

class ParameterDescription {

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
    private final boolean isOptional;
    private final ParamConstraint constraints;

    public ParameterDescription(final Element element, final RequestParam container, final ParamConstraint constraints, final Optional optional) {
        attributeName = element.getSimpleName().toString();

        name = computeName(container.name(), element.getSimpleName().toString(), container.role(), Utils.getDeclaredName(element));
        typeOrTemplateType = getTypeOrTemplate(element);
        typeWithoutTemplate = getTypeWithoutTemplate(element);
        typeWithoutTemplateSimple = getTypeWithoutTemplateSimple(element);
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

    private String getTypeWithoutTemplate(final Element attribute) {
        return attribute.asType().toString().replaceAll("\\<.*\\>", "");
    }

    private String getTypeOrTemplate(final Element attribute) {
        String string = attribute.asType().toString().replaceAll("\\<.*\\>", "");

        // TODO use inherit from collection
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

//    public final String getName() {
//        return name;
//    }
    
    public final String getAttributeName() {
        return attributeName;
    }

    public final String getNameStr() {
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

    public final ParamConstraintBinder getConstraints() {
        return new ParamConstraintBinder(constraints);
    }

    public static final class ParamConstraintBinder {
        public final boolean minIsExclusive;
        public final String min;
        public final String minErrorMsg;
        public final boolean maxIsExclusive;
        public final String max;
        public final String maxErrorMsg;
        public final String optionalErrorMsg;
        public final int precision;
        public final String precisionErrorMsg;
        public final int length;
        public final String LengthErrorMsg;

        public ParamConstraintBinder(final ParamConstraint constraint) {
            if (constraint != null) {
                minIsExclusive = constraint.minIsExclusive();
                min = constraint.min();
                minErrorMsg = Utils.getStr(constraint.minErrorMsg().value());
                maxIsExclusive = constraint.maxIsExclusive();
                max = constraint.max();
                maxErrorMsg = Utils.getStr(constraint.maxErrorMsg().value());
                optionalErrorMsg = Utils.getStr(constraint.optionalErrorMsg().value());
                precision = constraint.precision();
                precisionErrorMsg = Utils.getStr(constraint.precisionErrorMsg().value());
                length = constraint.length();
                LengthErrorMsg = Utils.getStr(constraint.LengthErrorMsg().value());
            } else {
                minIsExclusive = false;
                min = ParamConstraint.DEFAULT_MIN_STR;
                minErrorMsg = "ParamConstraint.DEFAULT_ERROR_MSG";
                maxIsExclusive = false;
                max = ParamConstraint.DEFAULT_MAX_STR;
                maxErrorMsg = "ParamConstraint.DEFAULT_ERROR_MSG";
                optionalErrorMsg = "ParamConstraint.DEFAULT_ERROR_MSG";
                precision = ParamConstraint.DEFAULT_PRECISION;
                precisionErrorMsg = "ParamConstraint.DEFAULT_ERROR_MSG";
                length = ParamConstraint.DEFAULT_LENGTH;
                LengthErrorMsg = "ParamConstraint.DEFAULT_ERROR_MSG";
            }
        }

    }

}
