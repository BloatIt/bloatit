package com.bloatit.framework.webprocessor.url.constraints;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.MessageFormater;

public abstract class MinMaxConstraint<V extends Comparable<V>> extends Constraint<V> {
    protected final boolean isExclusive;
    protected final Integer minMax;

    public MinMaxConstraint(final String message, final int minMax, final boolean isExclusive) {
        super(message);
        this.minMax = minMax;
        this.isExclusive = isExclusive;
    }

    @SuppressWarnings("cast")
    @Override
    protected void updateFormater(final MessageFormater formater, final V value) {
        formater.addParameter("%constraint%", String.valueOf(minMax));

        if (((Object) value) instanceof String) {
            formater.addParameter("%valueLength%", String.valueOf(String.class.cast(value).length()));
        }

    }

    @Override
    public boolean verify(final V value) {
        if (value == null) {
            return true;
        }

        // Weird hack :
        final Object hacked = value;

        if (hacked instanceof BigDecimal) {
            return cmp(isExclusive, (new BigDecimal(minMax).compareTo((BigDecimal) hacked)));
        }
        if (hacked instanceof String) {
            return cmp(isExclusive, (minMax.compareTo(((String) hacked).length())));
        }
        try {
            @SuppressWarnings("unchecked") final Comparable<V> theMin = value.getClass().cast(minMax);
            return cmp(isExclusive, theMin.compareTo(value));
        } catch (final ClassCastException e) {
            throw new BadProgrammerException("Constraint not allowed on this Class.", e);
        }
    }

    public abstract boolean cmp(boolean exclusive, int value);
}
