package com.bloatit.framework.webserver.url;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.utils.AsciiUtils;
import com.bloatit.framework.utils.HttpParameter;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.utils.SessionParameters;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.framework.webserver.annotations.Message;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.Message.What;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.components.form.FormFieldData;

public class UrlParameter<T, U> extends UrlNode {
    private final UrlParameterDescription<U> description;
    private final UrlParameterConstraints<U> constraints;

    private T value;
    private String strValue;
    private boolean conversionError;

    public UrlParameter(final T value, final UrlParameterDescription<U> description, final UrlParameterConstraints<U> constraints) {
        setValue(value); // Also set the defaultValue;
        this.description = description;
        this.constraints = constraints;
        this.conversionError = false;
    }

    @Override
    protected final void parseParameters(final Parameters params) {
        HttpParameter aValue = params.look(getName());
        if (aValue != null) {
            setValueFromHttpParameter(aValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void parseSessionParameters(SessionParameters params) {
        UrlParameter<?, ?> pick = params.look(getName());
        if (pick != null) {
            value = (T) pick.value;
            strValue = pick.strValue;
            conversionError = pick.conversionError;

            setValueFromHttpParameter(new HttpParameter(pick.getStringValue()));
        }
    }

    public String getStringValue() {
        if (!strValue.isEmpty()) {
            return strValue;
        }
        if (value != null && getRole() == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        try {
            return Loaders.toStr(value);
        } catch (final ConversionErrorException e) {
            return "";
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
        } catch (final ConversionErrorException e) {
            this.strValue = "";
        }
    }

    @SuppressWarnings("unchecked")
    private void setValueFromHttpParameter(final HttpParameter httpParam) {
        conversionError = false;
        strValue = httpParam.getSimpleValue();
        try {
            if (this.value instanceof List<?>) {
                StringBuilder sb = new StringBuilder();
                @SuppressWarnings("rawtypes")
                List casted = List.class.cast(this.value);
                for (String aValue : httpParam) {
                    // TODO make me works !
                    sb.append("&").append(getName()).append("=").append(aValue);
                    casted.add(Loaders.fromStr(getValueClass(), aValue));
                }
                strValue = sb.toString();
            } else {
                // TODO make sure this is working
                strValue = httpParam.getSimpleValue();
                if (value == null || getValueClass().equals(value.getClass())) {
                    setValue((T) Loaders.fromStr(getValueClass(), httpParam.getSimpleValue()));
                } else {
                    throw new FatalErrorException("Type mismatch. You are trying to convert a parameter using the wrong loader class.");
                }
            }
        } catch (final ConversionErrorException e) {
            conversionError = true;
        }
    }

    @Override
    public UrlParameter<T, U> clone() {
        return new UrlParameter<T, U>(value, description, constraints);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<UrlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Messages getMessages() {
        final Messages messages = new Messages();
        if (conversionError) {
            final Message message = new Message(getConversionErrorMsg(), getLevel(), What.CONVERSION_ERROR, getName(), getStringValue());
            messages.add(message);
        } else if (constraints != null) {
            constraints.computeConstraints((U) getValue(), getValueClass(), messages, getLevel(), getName(), getStringValue());
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
            this.setValueFromHttpParameter(new HttpParameter(aValue));
        }
    }

    private Class<U> getValueClass() {
        return description.getConvertInto();
    }

    private String getConversionErrorMsg() {
        return description.getConversionErrorMsg();
    }

    public final Level getLevel() {
        return description.getLevel();
    }

    public final String getName() {
        return description.getName();
    }

    public final Role getRole() {
        return description.getRole();
    }

    public FormFieldData<T> createFormFieldData() {
        return new FieldDataFromUrl<T, U>(this);
    }

    static class FieldDataFromUrl<T, U> implements FormFieldData<T> {

        private final UrlParameter<T, U> parameterFromSession;
        private final String name;

        public FieldDataFromUrl(UrlParameter<T, U> parameter) {
            super();
            name = parameter.getName();
            this.parameterFromSession = Context.getSession().pickParameter(parameter);
        }

        @Override
        public String getFieldName() {
            return name;
        }

        @Override
        public T getFieldDefaultValue() {
            if (parameterFromSession != null) {
                return parameterFromSession.getValue();
            }
            return null;

        }

        @Override
        public String getFieldDefaultValueAsString() {
            if (parameterFromSession != null) {
                return parameterFromSession.getStringValue();
            }
            return null;
        }

        @Override
        public Messages getFieldMessages() {
            if (parameterFromSession != null) {
                return parameterFromSession.getMessages();
            }
            return new Messages();
        }
    }
}
