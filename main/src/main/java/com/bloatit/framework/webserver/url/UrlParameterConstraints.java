package com.bloatit.framework.webserver.url;

import java.math.BigDecimal;
import java.util.EnumSet;

import com.bloatit.framework.webserver.annotations.Message;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.Message.What;

public class UrlParameterConstraints<U> {

    static class Param<T> {
        private final T value;
        private final String error;

        public Param(final T value, final String error) {
            super();
            this.value = value;
            this.error = error;
        }

        public final T getValue() {
            return value;
        }

        public final String getError() {
            return error;
        }
    }

    private final Param<Integer> min;
    private final Param<Integer> max;
    private final Param<Boolean> optional;
    private final Param<Integer> precision;
    private final Param<Integer> length;

    private enum ConstraintError {
        MIN_ERROR, MAX_ERROR, OPTIONAL_ERROR, PRECISION_ERROR, LENGTH_ERROR
    }

    public UrlParameterConstraints(final Integer min,
                                   final Integer max,
                                   final boolean optional,
                                   final int precision,
                                   final int length,
                                   final String minErrorMsg,
                                   final String maxErrorMsg,
                                   final String optErrorMsg,
                                   final String precisionErrorMsg,
                                   final String lenghtErrorMsg) {
        super();
        this.min = new Param<Integer>(min, minErrorMsg);
        this.max = new Param<Integer>(max, maxErrorMsg);
        this.optional = new Param<Boolean>(optional, optErrorMsg);
        this.precision = new Param<Integer>(precision, precisionErrorMsg);
        this.length = new Param<Integer>(length, lenghtErrorMsg);
    }

    public UrlParameterConstraints() {
        this.min = new Param<Integer>(ParamConstraint.DEFAULT_MIN, "");
        this.max = new Param<Integer>(ParamConstraint.DEFAULT_MAX, "");
        this.optional = new Param<Boolean>(ParamConstraint.DEFAULT_OPTIONAL, "");
        this.precision = new Param<Integer>(ParamConstraint.DEFAULT_PRECISION, "");
        this.length = new Param<Integer>(ParamConstraint.DEFAULT_LENGTH, "");
    }

    @SuppressWarnings("unchecked")
    public void computeConstraints(final U value,
                                   final Class<U> valueClass,
                                   final Messages messages,
                                   final Level level,
                                   final String name,
                                   final String strValue) {
        @SuppressWarnings("rawtypes")
        ComputeConstraint computeConstraint;
        if (valueClass.equals(Integer.class)) {
            computeConstraint = new ComputeIntegerConstraint<Integer>((UrlParameterConstraints<Integer>) this);
        } else if (valueClass.equals(BigDecimal.class)) {
            computeConstraint = new ComputeBigdecimalConstraint((UrlParameterConstraints<BigDecimal>) this);
        } else if (valueClass.equals(String.class)) {
            computeConstraint = new ComputeStringConstraint((UrlParameterConstraints<String>) this);
        } else {
            computeConstraint = new ComputeEverythingConstraint((UrlParameterConstraints<Object>) this);
        }
        updateMessages(computeConstraint.getConstraintErrors(value), messages, level, name, strValue);
    }

    private void updateMessages(final EnumSet<ConstraintError> enumSet,
                                final Messages messages,
                                final Level level,
                                final String name,
                                final String strValue) {
        if (enumSet.contains(ConstraintError.MIN_ERROR)) {
            messages.add(new Message(min.getError(), level, What.MIN_ERROR, name, strValue));
        }
        if (enumSet.contains(ConstraintError.MAX_ERROR)) {
            messages.add(new Message(max.getError(), level, What.MAX_ERROR, name, strValue));
        }
        if (enumSet.contains(ConstraintError.LENGTH_ERROR)) {
            messages.add(new Message(length.getError(), level, What.LENGTH_ERROR, name, strValue));
        }
        if (enumSet.contains(ConstraintError.OPTIONAL_ERROR)) {
            messages.add(new Message(optional.getError(), level, What.OPTIONAL_ERROR, name, strValue));
        }
        if (enumSet.contains(ConstraintError.PRECISION_ERROR)) {
            messages.add(new Message(precision.getError(), level, What.PRECISION_ERROR, name, strValue));
        }
    }

    static abstract class ComputeConstraint<T> {
        protected final UrlParameterConstraints<T> constraints;

        public ComputeConstraint(final UrlParameterConstraints<T> constraints) {
            super();
            this.constraints = constraints;
        }

        public EnumSet<ConstraintError> getConstraintErrors(final T value) {
            final EnumSet<ConstraintError> enumSet = EnumSet.noneOf(ConstraintError.class);
            if (value == null && !constraints.isOptional()) {
                enumSet.add(ConstraintError.OPTIONAL_ERROR);
            }
            // do not perform constraint checking if the values are null.
            if (value == null) {
                return enumSet;
            }
            if (constraints.getMin() != ParamConstraint.DEFAULT_MIN) {
                if (triggerMinConstraint(value)) {
                    enumSet.add(ConstraintError.MIN_ERROR);
                }
            }
            if (constraints.getMax() != ParamConstraint.DEFAULT_MAX) {
                if (triggerMaxConstraint(value)) {
                    enumSet.add(ConstraintError.MAX_ERROR);
                }
            }
            if (constraints.getPrecision() != ParamConstraint.DEFAULT_PRECISION) {
                if (triggerPrecisionConstraint(value)) {
                    enumSet.add(ConstraintError.PRECISION_ERROR);
                }
            }
            if (constraints.getLength() != ParamConstraint.DEFAULT_LENGTH) {
                if (triggerLengthConstraint(value)) {
                    enumSet.add(ConstraintError.LENGTH_ERROR);
                }
            }
            return enumSet;
        }

        public abstract boolean triggerMinConstraint(final T value);

        public abstract boolean triggerLengthConstraint(final T value);

        public abstract boolean triggerPrecisionConstraint(final T value);

        public abstract boolean triggerMaxConstraint(final T value);
    }

    static class ComputeStringConstraint extends ComputeConstraint<String> {
        public ComputeStringConstraint(final UrlParameterConstraints<String> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(final String value) {
            return value.length() < constraints.getMin();
        }

        @Override
        public boolean triggerPrecisionConstraint(final String value) {
            throw new UnsupportedOperationException("Cannot compute a precision constraint on a String value.");
        }

        @Override
        public boolean triggerMaxConstraint(final String value) {
            return value.length() > constraints.getMax();
        }

        @Override
        public boolean triggerLengthConstraint(final String value) {
            return value.length() == constraints.getLength();
        }
    }

    static class ComputeIntegerConstraint<T extends Comparable<Integer>> extends ComputeConstraint<T> {
        public ComputeIntegerConstraint(final UrlParameterConstraints<T> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(final T value) {
            return value.compareTo(constraints.getMin()) < 0;
        }

        @Override
        public boolean triggerMaxConstraint(final T value) {
            return value.compareTo(constraints.getMax()) > 0;
        }

        @Override
        public boolean triggerLengthConstraint(final T value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(final T value) {
            throw new UnsupportedOperationException("Cannot compute a precision constraint on this parameter.");
        }
    }

    static class ComputeBigdecimalConstraint extends ComputeConstraint<BigDecimal> {
        public ComputeBigdecimalConstraint(final UrlParameterConstraints<BigDecimal> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(final BigDecimal value) {
            return value.compareTo(BigDecimal.valueOf(constraints.getMin())) < 0;
        }

        @Override
        public boolean triggerLengthConstraint(final BigDecimal value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(final BigDecimal value) {
            return value.stripTrailingZeros().scale() > constraints.getPrecision();
        }

        @Override
        public boolean triggerMaxConstraint(final BigDecimal value) {
            return value.compareTo(BigDecimal.valueOf(constraints.getMax())) > 0;
        }
    }

    static class ComputeEverythingConstraint extends ComputeConstraint<Object> {
        public ComputeEverythingConstraint(final UrlParameterConstraints<Object> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(final Object value) {
            return value.toString().length() < constraints.getMin();
        }

        @Override
        public boolean triggerLengthConstraint(final Object value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(final Object value) {
            return false;
        }

        @Override
        public boolean triggerMaxConstraint(final Object value) {
            return value.toString().length() > constraints.getMax();
        }
    }

    public final Integer getMin() {
        return min.getValue();
    }

    public final Integer getMax() {
        return max.getValue();
    }

    public final boolean isOptional() {
        return optional.getValue();
    }

    public final int getPrecision() {
        return precision.getValue();
    }

    public final int getLength() {
        return length.getValue();
    }
}
