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
    private Role role;

    public Parameter(final Messages messages, final String name, final Object value, final Role role, final Level level, final String message) {
        this.messages = messages;
        this.name = name;
        this.role = role;
        this.value = value;
        this.level = level;
        this.message = message;
    }

    public Role getRole() {
        return role;
    }

    private void addToMessages(What what) {
        message.replaceAll("%param", name);
        message.replaceAll("%value", getStringValue());
        messages.add(new Message(level, what, message));
    }

    public String getStringValue() {
        if (value == null) {
            addToMessages(What.NOT_FOUND);
        } else if (role == Role.PRETTY) {
            return String.class.cast(value).replaceAll("[ ,\\.\\'\\\"\\&\\?]", "-").subSequence(0, 80).toString();
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
        if (value.getClass().equals(String.class)) {
            value = string;
            return;
        }
        try {
            value = Loaders.fromStr(value.getClass(), string);
        } catch (ConversionErrorException e) {
            addToMessages(What.CONVERSION_ERROR);
        }
    }
}
