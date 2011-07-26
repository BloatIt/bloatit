//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.url;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.AsciiUtils;
import com.bloatit.framework.utils.parameters.HttpParameter;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.utils.parameters.SessionParameters;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.annotations.MessageFormater;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.constraints.Constraint;

public class UrlParameter<T, U> extends UrlNode {
    private final UrlParameterDescription<U> description;
    private List<Constraint<U>> constraints = new ArrayList<Constraint<U>>();
    private final Messages customMessages = new Messages();

    private T value;
    private String strValue;
    private boolean conversionError;

    @SuppressWarnings("unchecked")
    public UrlParameter(final T value, final UrlParameterDescription<U> description) {
        setValue(value, false); // Also set the string value;
        this.description = description;
        this.conversionError = false;
        if (description.getDefaultValue() != null && value == null) {
            try {
                this.value = (T) Loaders.fromStr(getValueClass(), description.getDefaultValue());
                this.strValue = description.getDefaultValue();
            } catch (final ConversionErrorException e) {
                throw new BadProgrammerException("conversion error ! Make sure the default value is correct.", e);
            }
        }
    }

    public final void addConstraint(final Constraint<U> constraint) {
        constraints.add(constraint);
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

    @Override
    protected void constructUrl(final StringBuilder sb) {
        final String stringValue = getStringValue();
        if (getRole() == Role.GET || getRole() == Role.PRETTY || getRole() == Role.POSTGET) {
            if (!stringValue.isEmpty() && !stringValue.equals(getDefaultValue()) && value != null) {
                final URLCodec urlCodec = new URLCodec();
                try {
                    sb.append(urlCodec.encode(getName())).append('=').append(urlCodec.encode(stringValue));
                } catch (final EncoderException e) {
                    throw new BadProgrammerException(e);
                }
            }
        }
    }

    @Override
    protected void getParametersAsStrings(final Parameters parameters) {
        final String stringValue = getStringValue();
        if (getRole() == Role.GET || getRole() == Role.PRETTY || getRole() == Role.POSTGET) {
            if (!stringValue.isEmpty() && !stringValue.equals(getDefaultValue()) && value != null) {
                parameters.add(getName(), stringValue);
            }
        }
    }

    @Override
    public UrlParameter<T, U> clone() {
        final UrlParameter<T, U> urlParameter = new UrlParameter<T, U>(value, description);
        urlParameter.constraints = this.constraints;
        return urlParameter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<UrlNode> iterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    public final String getName() {
        return description.getName();
    }

    public final Role getRole() {
        return description.getRole();
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

    public String getDefaultSuggestedValue() {
        return description.getSuggestedValue();
    }

    public T getValue() {
        return value;
    }

    public String getStringValue() {
        if (strValue != null && !strValue.isEmpty()) {
            if (getRole() == Role.PRETTY) {
                return makeStringPretty(strValue);
            }
            return strValue;
        }
        if (value != null && getRole() == Role.PRETTY) {
            return makeStringPretty(String.class.cast(value));
        }
        return toString(value);
    }

    public final void setValue(final T value) {
        setValue(value, false);
    }
    
    public final void setValue(final T value, boolean force) {
        setValueUnprotected(value);
        if(!force) {
            final Messages messages = getMessages();
            if (messages.size() - customMessages.size() > 0) {
                final StringBuilder sb = new StringBuilder();
                for (final Message message : messages) {
                    sb.append(message.getMessage()).append(" [").append(value).append("] && ");
                }
                throw new BadProgrammerException(sb.toString());
            }
        }
    }

    public boolean hasConversionError() {
        return conversionError;
    }

    public boolean hasError() {
        return !getMessages().isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Messages getMessages() {
        final Messages messages = new Messages();
        messages.addAll(customMessages);

        if (conversionError) {
            final Message message = new Message(getConversionErrorMsg(), new MessageFormater(getName(), getStringValue()));
            messages.add(message);
        } else {
            for (final Constraint<U> constraint : constraints) {
                final Message message = constraint.getMessage((U) getValue(), new MessageFormater(getName(), getStringValue()));
                if (message != null) {
                    messages.add(message);
                }
            }
        }
        return messages;
    }

    public void addErrorMessage(final String message) {
        customMessages.add(new Message(message));
    }

    @Override
    @Deprecated
    public void addParameter(final String aName, final String aValue) {
        if (this.getName().equals(aName)) {
            this.setValueFromHttpParameter(new HttpParameter(aValue));
        }
    }

    private String makeStringPretty(final String theValue) {
        String tmp = theValue.replaceAll("[ ,\\.\\'\\\"\\&\\?\r\n%\\*\\!:\\^¨\\+#/]", "-");
        tmp = tmp.replaceAll("--+", "-");
        tmp = tmp.subSequence(0, Math.min(tmp.length(), 80)).toString();
        tmp = tmp.replaceAll("-+$", "");
        tmp = tmp.toLowerCase();
        tmp = AsciiUtils.convertNonAscii(tmp);
        return tmp;
    }

    private final void setValueUnprotected(final T value) {
        this.value = value;
        strValue = toString(value);
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
                    sb.append('&').append(getName()).append('=').append(aValue);
                    casted.add(Loaders.fromStr(description.getConvertInto(), aValue));
                }
                strValue = sb.toString();
            } else {
                strValue = httpParam.getSimpleValue();
                if (value == null || getValueClass().isAssignableFrom(value.getClass())) {
                    setValueUnprotected((T) Loaders.fromStr(getValueClass(), httpParam.getSimpleValue()));
                } else {
                    throw new BadProgrammerException("Type mismatch. " + getValueClass().getSimpleName() + " =! " + value.getClass().getSimpleName()
                            + " You are trying to convert a parameter using the wrong loader class.");
                }
            }
        } catch (final ConversionErrorException e) {
            conversionError = true;
        }
    }

    @SuppressWarnings("unchecked")
    private String toString(final T value) {
        // If it is a list then it's a list of parameters.
        if (value instanceof List) {
            final StringBuilder sb = new StringBuilder();
            for (final U elem : ((List<U>) value)) {
                sb.append('&').append(getName()).append('=').append(Loaders.toStr(elem));
            }
            return sb.toString();
        }
        return Loaders.toStr(value);
    }

    private Class<U> getValueClass() {
        return description.getConvertInto();
    }

    private String getConversionErrorMsg() {
        return description.getConversionErrorMsg();
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
        private FieldDataFromUrl(final UrlParameter<T, U> parameter) {
            super();
            name = parameter.getName();

            final UrlParameter<T, U> sessionParam = Context.getSession().pickParameter(parameter);
            if (sessionParam != null) {
                suggestedValue = sessionParam.getSuggestedValue();
                messages = sessionParam.getMessages();
            } else {
                suggestedValue = parameter.getSuggestedValue();
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
