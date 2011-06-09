package com.bloatit.framework.webprocessor.url.constraints;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.MessageFormater;

public class PrecisionConstraint<V extends Comparable<V>> extends Constraint<V> {
    private final int precision;

    public PrecisionConstraint(final String message, final int precision) {
        super(message);
        this.precision = precision;
    }

    @Override
    public boolean verify(final V value) {
        if (value == null) {
            return true;
        }

        // Weird hack :
        final Object hacked = value;

        if (hacked instanceof BigDecimal) {
            return ((BigDecimal) hacked).stripTrailingZeros().scale() <= precision;
        }
        throw new BadProgrammerException("Precision is not supported for this class.");
    }

    @Override
    protected void updateFormater(final MessageFormater formater) {
        formater.addParameter("%constraint%", String.valueOf(precision));
    }
}
