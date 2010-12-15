package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

public class Parameter {
    private String message;
    private Level level;

    private String name;
    private Object value;
    private final Class<?> valueClass;
    private Role role;
    private What what;

    public Parameter(final String name, final Object value, final Class<?> valueClass, final Role role, final Level level, final String message) {
        this.name = name;
        this.role = role;
        this.value = value;
        this.valueClass = valueClass;
        this.level = level;
        this.message = message;
    }

    public Role getRole() {
        return role;
    }

    public Message getMessage() {
        if (what == What.NO_ERROR) {
            return null;
        }
        String errorMsg = message.replaceAll("%param", name);
        if (value != null) {
            try {
                errorMsg = errorMsg.replaceAll("%value", Loaders.toStr(value));
            } catch (ConversionErrorException e) {
                errorMsg = errorMsg.replaceAll("%value", "null");
            }
        } else {
            errorMsg = errorMsg.replaceAll("%value", "null");
        }
        return new Message(level, what, errorMsg);
    }

    private String makeStringPretty(String value) {
        value = value.replaceAll("[ ,\\.\\'\\\"\\&\\?\r\n%\\*\\!:\\^¨]", "-");
        value = value.replaceAll("(--)+", "-");
        value = value.replaceAll("(--)+", "-");
        value = value.subSequence(0, Math.min(value.length(), 80)).toString();
        value = value.replaceAll("-+$", "");
        value = value.toLowerCase();
        return value;
    }

    public String getStringValue() {
        if (value == null) {
            what = What.NOT_FOUND;
        } else if (role == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        try {
            return Loaders.toStr(value);
        } catch (ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
            return "null";
        }
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void valueFromString(String string) {
        if (valueClass.equals(String.class)) {
            value = string;
            return;
        }
        try {
            value = Loaders.fromStr(valueClass, string);
        } catch (ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
        }
    }
}
