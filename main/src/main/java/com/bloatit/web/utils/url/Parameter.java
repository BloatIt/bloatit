package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.AsciiUtils;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

public class Parameter<T> {
    private final String message;
    private final Level level;

    private final String name;
    private T value;
    private final Class<T> valueClass;
    private final Role role;
    private What what;

    public Parameter(final String name, final T value, final Class<T> valueClass, final Role role, final Level level, final String message) {
        this.name = name;
        this.role = role;
        this.value = value;
        this.valueClass = valueClass;
        this.level = level;
        this.message = message;
        this.what = What.NO_ERROR;
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
            } catch (final ConversionErrorException e) {
                errorMsg = errorMsg.replaceAll("%value", "null");
            }
        } else {
            errorMsg = errorMsg.replaceAll("%value", "null");
        }
        return new Message(level, what, errorMsg);
    }

    private String makeStringPretty(String value) {
        value = value.replaceAll("[ ,\\.\\'\\\"\\&\\?\r\n%\\*\\!:\\^¨\\+]", "-");
        value = value.replaceAll("(---)+", "-");
        value = value.replaceAll("(--)+", "-");
        value = value.subSequence(0, Math.min(value.length(), 80)).toString();
        value = value.replaceAll("-+$", "");
        value = value.toLowerCase();
        value = AsciiUtils.convertNonAscii(value);
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
        } catch (final ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
            return "null";
        }
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void valueFromString(final String string) {
        try {
            value = Loaders.fromStr(valueClass, string);
        } catch (final ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
        }
    }
}
