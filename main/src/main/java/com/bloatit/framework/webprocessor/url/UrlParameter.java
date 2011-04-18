package com.bloatit.framework.webprocessor.url;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.AsciiUtils;
import com.bloatit.framework.utils.parameters.HttpParameter;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.utils.parameters.SessionParameters;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.annotations.Message.What;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.context.Context;

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
        if (description.getDefaultValue() != null && value == null) {
            setValueFromString(description.getDefaultValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void setValueFromString(final String strValue) {
        try {
            this.value = (T) Loaders.fromStr(getValueClass(), strValue);
            this.strValue = strValue;
        } catch (final ConversionErrorException e) {
            Log.framework().fatal("conversion error ! Make sure the default value is correct.", e);
        }
    }

    @Override
    protected final void parseParameters(final Parameters params) {
        final HttpParameter aValue = params.look(getName());
        if (aValue != null) {
            setValueFromHttpParameter(aValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void parseSessionParameters(final SessionParameters params) {
        final UrlParameter<?, ?> pick = params.look(getName());
        if (pick != null && pick.value != null && getValueClass().isAssignableFrom(pick.value.getClass())) {
            value = (T) pick.value;
            strValue = pick.strValue;
            conversionError = pick.conversionError;

            setValueFromHttpParameter(new HttpParameter(pick.getStringValue()));
        }
    }

    public String getStringValue() {
        if (strValue != null && !strValue.isEmpty()) {
            return strValue;
        }
        if (value != null && getRole() == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        try {
            return toString(value);
        } catch (final ConversionErrorException e) {
            if (value != null) {
                Log.framework().warn("Error converting user input, from " + value.getClass().getSimpleName() + " to string.", e);
            } else {
                Log.framework().warn("Error converting user input, from null to string.", e);
            }
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
            strValue = toString(value);
        } catch (final ConversionErrorException e) {
            if (value != null) {
                Log.framework().warn("Error converting user input, from " + value.getClass().getSimpleName() + " to string.", e);
            } else {
                Log.framework().warn("Error converting user input, from null to string.", e);
            }
            this.strValue = "";
        }
    }


    @SuppressWarnings("unchecked")
    private void setValueFromHttpParameter(final HttpParameter httpParam) {
        conversionError = false;
        strValue = httpParam.getSimpleValue();
        try {
            if (this.value instanceof List) {
                final StringBuilder sb = new StringBuilder();
                @SuppressWarnings("rawtypes") final List casted = List.class.cast(this.value);
                for (final String aValue : httpParam) {
                    // TODO make me works !
                    sb.append("&").append(getName()).append("=").append(aValue);
                    casted.add(Loaders.fromStr(description.getConvertInto(), aValue));
                }
                strValue = sb.toString();
            } else {
                // TODO make sure this is working
                strValue = httpParam.getSimpleValue();
                if (value == null || getValueClass().isAssignableFrom(value.getClass())) {
                    setValue((T) Loaders.fromStr(getValueClass(), httpParam.getSimpleValue()));
                } else {
                    throw new BadProgrammerException("Type mismatch. " + getValueClass().getSimpleName() + " =! " + value.getClass().getSimpleName()
                            + " You are trying to convert a parameter using the wrong loader class.");
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
    
    public boolean hasError() {
        return !getMessages().isEmpty();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Messages getMessages() {
        final Messages messages = new Messages();
        if (conversionError) {
            final Message message = new Message(getConversionErrorMsg(), What.CONVERSION_ERROR, getName(), getStringValue());
            messages.add(message);
        } else if (constraints != null) {
            constraints.computeConstraints((U) getValue(), getValueClass(), messages, getName(), getStringValue());
        }
        return messages;
    }

    @Override
    protected void constructUrl(final StringBuilder sb) {
        final String stringValue = getStringValue();
        if (getRole() == Role.GET || getRole() == Role.PRETTY || getRole() == Role.POSTGET) {
            if (!stringValue.isEmpty() && !stringValue.equals(getDefaultValue()) && value != null) {
                sb.append("/").append(getName()).append("-").append(stringValue);
            }
        }
    }

    @Override
    protected void getStringParameters(final Parameters parameters) {
        final String stringValue = getStringValue();
        if (getRole() == Role.GET || getRole() == Role.PRETTY || getRole() == Role.POSTGET) {
            if (!stringValue.isEmpty() && !stringValue.equals(getDefaultValue()) && value != null) {
                parameters.add(getName(), stringValue);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private String toString(final T value) throws ConversionErrorException {
        // If it is a list then it's a list of parameters.
        if (value instanceof List) {
            final StringBuilder sb = new StringBuilder();
            for (final U elem : ((List<U>) value)) {
                sb.append("&").append(getName()).append("=").append(Loaders.toStr(elem));
            }
            return sb.toString();
        }
        return Loaders.toStr(value);
    }

    public String getDefaultValue() {
        return description.getDefaultValue();
    }

    public String getSuggestedValue() {
        if (strValue != null && !strValue.isEmpty()) {
            return strValue;
        }
        final String suggestedValue = description.getSuggestedValue();
        if (suggestedValue == null) {
            return getDefaultValue();
        }
        return suggestedValue;
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

    public final String getName() {
        return description.getName();
    }

    public final Role getRole() {
        return description.getRole();
    }

    public FieldData pickFieldData() {
        return new FieldDataFromUrl<T, U>(this);
    }

    static class FieldDataFromUrl<T, U> implements FieldData {

        private final String name;
        private final String suggestedValue;
        private final Messages messages;

        /**
         * Try to locate <code>parameter</code> in the session. If found use
         * this one, else use the parameter passed in the constructor.
         *
         * @param parameter a parameter to find or use.
         */
        public FieldDataFromUrl(final UrlParameter<T, U> parameter) {
            super();
            name = parameter.getName();

            final UrlParameter<T, U> sessionParam = Context.getSession().pickParameter(parameter);
            if (sessionParam != null) {
                suggestedValue = sessionParam.getSuggestedValue();
                messages = sessionParam.getMessages();
            } else {
                suggestedValue = null;
                messages = new Messages();
            }
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getSuggestedValue() {
            return suggestedValue;
        }

        @Override
        public Messages getErrorMessages() {
            return messages;
        }
    }

}
