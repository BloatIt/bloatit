package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.RequestParam.Role;

public final class UrlParameterDescription<T> {

    private final String name;
    private final Class<T> valueClass;
    private final Role role;
    private final String defaultValue;

    public UrlParameterDescription(String name, Class<T> valueClass, Role role, String defaultValue) {
        super();
        this.name = name;
        this.valueClass = valueClass;
        this.role = role;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    public Role getRole() {
        return role;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
