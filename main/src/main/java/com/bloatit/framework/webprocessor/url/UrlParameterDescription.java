package com.bloatit.framework.webprocessor.url;

import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;

public final class UrlParameterDescription<U> {

    private final String name;
    private final Class<U> convertInto;
    private final Role role;
    private final String defaultValue;
    private final String suggestedValue;
    private final String conversionErrorMsg;
    private final boolean isOptional;

    public UrlParameterDescription(final String name,
                                   final Class<U> convertInto,
                                   final Role role,
                                   final String defaultValue,
                                   final String suggestedValue,
                                   final String conversionErrorMsg,
                                   final boolean isOptional) {
        super();
        this.name = name;
        this.convertInto = convertInto;
        this.role = role;
        this.defaultValue = defaultValue;
        this.suggestedValue = suggestedValue;
        this.conversionErrorMsg = conversionErrorMsg;
        this.isOptional = isOptional;
    }

    public final String getName() {
        return name;
    }

    public final Class<U> getConvertInto() {
        return convertInto;
    }

    public final Role getRole() {
        return role;
    }

    public final String getDefaultValue() {
        return defaultValue;
    }

    public final String getSuggestedValue() {
        return suggestedValue;
    }

    public final String getConversionErrorMsg() {
        return conversionErrorMsg;
    }

    public boolean isOptional() {
        return isOptional;
    }

}
