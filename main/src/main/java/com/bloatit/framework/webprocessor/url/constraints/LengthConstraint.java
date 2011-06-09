package com.bloatit.framework.webprocessor.url.constraints;

public class LengthConstraint<V extends Comparable<V>> extends MinMaxConstraint<V> {
    public LengthConstraint(final String message, final int length) {
        super(message, length, true);
    }

    @Override
    public boolean cmp(final boolean exclusive, final int value) {
        return value == 0;
    }
}