package com.bloatit.web.utils.url;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.AsciiUtils;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;
import com.bloatit.web.utils.annotations.Messages;

public final class UrlParameter<T> extends UrlNode {
    // TODO optimize me (Use some static class ? for final parameters)
    // Description of the level and messages for this parameter
    private final String message;
    private final Level level;
    private What what; // can change during the parsing.

    // Description of the parameter
    private final String name;
    private final Class<T> valueClass;
    private final Role role;
    private final String defaultValue;

    // the value
    private T value;

    public UrlParameter(final String name,
                        final T value,
                        final String defaultValue,
                        final Class<T> valueClass,
                        final Role role,
                        final Level level,
                        final String message) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.role = role;
        this.value = value;
        this.valueClass = valueClass;
        this.level = level;
        this.message = Context.tr(message);
        if (value == null && level == Level.ERROR) {
            this.what = What.NOT_FOUND;
        } else {
            this.what = What.NO_ERROR;
        }
    }

    @Override
    protected final void parseParameters(final Parameters params, final boolean pickValue) {
        final String aValue;
        if (pickValue) {
            aValue = params.pick(getName());
        } else {
            aValue = params.look(getName());
        }
        if (aValue != null) {
            setValueFromString(aValue);
        }
    }

    public Role getRole() {
        return role;
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

    private String makeStringPretty(final String theValue) {
        String tmp = theValue.replaceAll("[ ,\\.\\'\\\"\\&\\?\r\n%\\*\\!:\\^¨\\+]", "-");
        tmp = tmp.replaceAll("--+", "-");
        tmp = tmp.subSequence(0, Math.min(tmp.length(), 80)).toString();
        tmp = tmp.replaceAll("-+$", "");
        tmp = tmp.toLowerCase();
        tmp = AsciiUtils.convertNonAscii(tmp);
        return tmp;
    }

    public T getValue() {
        return value;
    }

    public void setValue(final T value) {
        what = What.NO_ERROR;
        this.value = value;
    }

    private void setValueFromString(final String string) {
        try {
            setValue(Loaders.fromStr(valueClass, string));
        } catch (final ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public UrlParameter<T> clone() {
        return new UrlParameter<T>(name, value, defaultValue, valueClass, role, level, message);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<UrlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @Override
    public Messages getMessages() {
        final Messages messages = new Messages();
        final Message errorMessage = getMessage();
        if (errorMessage != null) {
            messages.add(errorMessage);
        }
        return messages;
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

    @Override
    protected void constructUrl(final StringBuilder sb) {
        final String stringValue = getStringValue();
        if (role == Role.GET || role == Role.PRETTY) {
            if (!stringValue.isEmpty() && !stringValue.equals(defaultValue) && value != null) {
                sb.append("/").append(getName()).append("-").append(stringValue);
            }
        }
    }

    @Override
    @Deprecated
    public void addParameter(final String aName, final String aValue) {
        if (this.name.equals(aName)) {
            this.setValueFromString(aValue);
        }
    }
}
