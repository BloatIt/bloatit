package com.bloatit.web.utils.url;

import java.util.Collections;
import java.util.Iterator;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.AsciiUtils;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;
import com.bloatit.web.utils.annotations.Messages;

public final class UrlParameter<T> extends UrlNode {
    // TODO optimize me (Use some static classes ?)
    private final UrlParameterDescription<T> description;
    private final UrlParameterConstraints<T> constraints;

    private T value;
    private String strValue;
    private boolean conversionError;

    public UrlParameter(final T value, final UrlParameterDescription<T> description, UrlParameterConstraints<T> constraints) {
        setValue(value); // Also set the defaultValue;
        this.description = description;
        this.constraints = constraints;
        this.conversionError = false;
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
        return description.getRole();
    }

    public String getStringValue() {
        if (!strValue.isEmpty()){
            return strValue;
        }
        if (value != null && getRole() == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        try {
            return Loaders.toStr(value);
        } catch (final ConversionErrorException e) {
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

    public final void setValue(final T value) {
        this.value = value;
        try {
            this.strValue = Loaders.toStr(value);
        } catch (ConversionErrorException e) {
            this.strValue = "";
        }
    }

    private void setValueFromString(final String string) {
        try {
            conversionError = false;
            setValue(Loaders.fromStr(description.getValueClass(), string));
        } catch (final ConversionErrorException e) {
            conversionError = true;
        }
    }

    public String getName() {
        return description.getName();
    }

    @Override
    public UrlParameter<T> clone() {
        return new UrlParameter<T>(value, description, constraints);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<UrlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @Override
    public Messages getMessages() {
        final Messages messages = new Messages();
        if (conversionError) {
            Message message = new Message(description.getConversionErrorMsg(), description.getLevel(), What.CONVERSION_ERROR, description.getName(),
                    getStringValue());
            messages.add(message);
        } else if (constraints != null) {
            constraints.computeConstraints(getValue(),
                                           description.getValueClass(),
                                           messages,
                                           description.getLevel(),
                                           description.getName(),
                                           getStringValue());
        }
        return messages;
    }

    @Override
    protected void constructUrl(final StringBuilder sb) {
        final String stringValue = getStringValue();
        if (getRole() == Role.GET || getRole() == Role.PRETTY) {
            if (!stringValue.isEmpty() && !stringValue.equals(getDefaultValue()) && value != null) {
                sb.append("/").append(getName()).append("-").append(stringValue);
            }
        }
    }

    public String getDefaultValue() {
        return description.getDefaultValue();
    }

    @Override
    @Deprecated
    public void addParameter(final String aName, final String aValue) {
        if (this.getName().equals(aName)) {
            this.setValueFromString(aValue);
        }
    }
}
