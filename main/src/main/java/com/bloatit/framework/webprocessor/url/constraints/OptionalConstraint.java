package com.bloatit.framework.webprocessor.url.constraints;

import com.bloatit.framework.webprocessor.annotations.MessageFormater;

public class OptionalConstraint<V> extends Constraint<V> {

    public OptionalConstraint(final String message) {
        super(message);
    }

    @Override
    public boolean verify(final V value) {
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        return value != null;
    }

    @Override
    protected void updateFormater(final MessageFormater formater) {
        // Nothing to do.
    }
}