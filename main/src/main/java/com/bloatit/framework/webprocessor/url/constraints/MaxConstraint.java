package com.bloatit.framework.webprocessor.url.constraints;

public class MaxConstraint<V extends Comparable<V>> extends MinMaxConstraint<V> {
    public MaxConstraint(final String message, final int max, final boolean isExclusive) {
        super(message, max, isExclusive);
    }

    @Override
    public boolean cmp(final boolean exclusive, final int value) {
        return Constraint.cmp(false, exclusive, value);
    }
}