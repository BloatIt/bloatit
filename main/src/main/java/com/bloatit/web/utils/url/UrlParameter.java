package com.bloatit.web.utils.url;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.AsciiUtils;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;
import com.bloatit.web.utils.annotations.Messages;

public final class UrlParameter<T> extends UrlNode {
    private final String message;
    private final Level level;

    private final String name;
    private T value;
    private final Class<T> valueClass;
    private final Role role;
    private What what;

    public UrlParameter(final String name, final T value, final Class<T> valueClass, final Role role, final Level level, final String message) {
        this.name = name;
        this.role = role;
        this.value = value;
        this.valueClass = valueClass;
        this.level = level;
        this.message = message;
        if (value == null && level == Level.ERROR) {
            this.what = What.NOT_FOUND;
        } else {
            this.what = What.NO_ERROR;
        }
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

    private String makeStringPretty(final String theValue) {
        String tmp = theValue.replaceAll("[ ,\\.\\'\\\"\\&\\?\r\n%\\*\\!:\\^¨\\+]", "-");
        tmp = tmp.replaceAll("--+", "-");
        tmp = tmp.subSequence(0, Math.min(tmp.length(), 80)).toString();
        tmp = tmp.replaceAll("-+$", "");
        tmp = tmp.toLowerCase();
        tmp = AsciiUtils.convertNonAscii(tmp);
        return tmp;
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

    public void setValue(final T value) {
        what = What.NO_ERROR;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void valueFromString(final String string) {
        try {
            setValue(Loaders.fromStr(valueClass, string));
        } catch (final ConversionErrorException e) {
            what = What.CONVERSION_ERROR;
        }
    }

    @Override
    public UrlParameter<T> clone() {
        return new UrlParameter<T>(name, value, valueClass, role, level, message);
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

    @Override
    protected void parseParameters(final Parameters params, final boolean pickValue) {
        final String value;
        if (pickValue) {
            value = params.pick(getName());
        } else {
            value = params.look(getName());
        }
        if (value != null) {
            valueFromString(value);
        }
    }

    @Override
    public void addParameter(final String name, final String value) {
        if (this.name == name) {
            this.valueFromString(value);
        }
    }

    @Override
    protected void constructUrl(final StringBuilder sb) {
        final String stringValue = getStringValue();
        if (role == Role.GET || role == Role.PRETTY) {
            if (!stringValue.isEmpty()) {
                sb.append("/").append(getName()).append("-").append(stringValue);
            }
        }
    }
}
