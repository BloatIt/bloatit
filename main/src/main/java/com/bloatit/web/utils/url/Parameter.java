package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class Parameter {
    private Messages messages;
    private String message;
    private Level level;

    private String name;
    private Object value;
    private final Class<?> valueClass;
    private Role role;

    public Parameter(final Messages messages,
                     final String name,
                     final Object value,
                     final Class<?> valueClass,
                     final Role role,
                     final Level level,
                     final String message) {
        this.messages = messages;
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

    private void addToMessages(What what) {
        message.replaceAll("%param", name);
        if (value != null) {
            try {
                message.replaceAll("%value", Loaders.toStr(value));
            } catch (ConversionErrorException e) {
                message.replaceAll("%value", "null");
            }
        } else {
            message.replaceAll("%value", "null");
        }
        messages.add(new Message(level, what, message));
    }

    private String makeStringPretty(String value){
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
            addToMessages(What.NOT_FOUND);
        } else if (role == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        try {
            return Loaders.toStr(value);
        } catch (ConversionErrorException e) {
            addToMessages(What.CONVERSION_ERROR);
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
            addToMessages(What.CONVERSION_ERROR);
        }
    }
}
