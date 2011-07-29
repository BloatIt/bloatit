package com.bloatit.framework.webprocessor.url.constraints;

public class MinConstraint<V extends Comparable<V>> extends MinMaxConstraint<V> {
    public MinConstraint(final String message, final int min, final boolean isExclusive) {
        super(message, min, isExclusive);
    }

    @Override
    public boolean cmp(final boolean exclusive, final int value) {
        return Constraint.cmp(true, exclusive, value);
    }
}
