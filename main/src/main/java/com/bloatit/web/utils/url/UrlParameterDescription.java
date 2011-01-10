package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public final class UrlParameterDescription<T> {

    private final String name;
    private final Class<T> valueClass;
    private final Role role;
    private final String defaultValue;
    private final String conversionErrorMsg;
    private final Message.Level level;

    public UrlParameterDescription(String name, Class<T> valueClass, Role role, String defaultValue, String conversionErrorMsg, Level level) {
        super();
        this.name = name;
        this.valueClass = valueClass;
        this.role = role;
        this.defaultValue = defaultValue;
        this.conversionErrorMsg = conversionErrorMsg;
        this.level = level;
    }

    public final String getName() {
        return name;
    }

    public final Class<T> getValueClass() {
        return valueClass;
    }

    public final Role getRole() {
        return role;
    }

    public final String getDefaultValue() {
        return defaultValue;
    }

    public final String getConversionErrorMsg() {
        return conversionErrorMsg;
    }

    public final Message.Level getLevel() {
        return level;
    }

}
