package com.bloatit.web.utils.url;

import java.math.BigDecimal;
import java.util.EnumSet;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;
import com.bloatit.web.annotations.ParamConstraint;
import com.bloatit.web.utils.annotations.Messages;

public class UrlParameterConstraints<U> {

    static class Param<T> {
        private final T value;
        private final String error;

        public Param(T value, String error) {
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

    public UrlParameterConstraints(Integer min,
                                   Integer max,
                                   boolean optional,
                                   int precision,
                                   int length,
                                   String minErrorMsg,
                                   String maxErrorMsg,
                                   String optErrorMsg,
                                   String precisionErrorMsg,
                                   String lenghtErrorMsg) {
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
    public void computeConstraints(U value, Class<U> valueClass, Messages messages, Level level, String name, String strValue) {
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

    private void updateMessages(EnumSet<ConstraintError> enumSet, Messages messages, Level level, String name, String strValue) {
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

        public ComputeConstraint(UrlParameterConstraints<T> constraints) {
            super();
            this.constraints = constraints;
        }

        public EnumSet<ConstraintError> getConstraintErrors(final T value) {
            EnumSet<ConstraintError> enumSet = EnumSet.noneOf(ConstraintError.class);
            if (value == null && constraints.isOptional() == false) {
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
        public ComputeStringConstraint(UrlParameterConstraints<String> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(String value) {
            return value.length() < constraints.getMin();
        }

        @Override
        public boolean triggerPrecisionConstraint(String value) {
            throw new UnsupportedOperationException("Cannot compute a precision constraint on a String value.");
        }

        @Override
        public boolean triggerMaxConstraint(String value) {
            return value.length() > constraints.getMax();
        }

        @Override
        public boolean triggerLengthConstraint(String value) {
            return value.length() == constraints.getLength();
        }
    }

    static class ComputeIntegerConstraint<T extends Comparable<Integer>> extends ComputeConstraint<T> {
        public ComputeIntegerConstraint(UrlParameterConstraints<T> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(T value) {
            return value.compareTo(constraints.getMin()) < 0;
        }

        @Override
        public boolean triggerMaxConstraint(T value) {
            return value.compareTo(constraints.getMax()) > 0;
        }

        @Override
        public boolean triggerLengthConstraint(T value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(T value) {
            throw new UnsupportedOperationException("Cannot compute a precision constraint on this parameter.");
        }
    }

    static class ComputeBigdecimalConstraint extends ComputeConstraint<BigDecimal> {
        public ComputeBigdecimalConstraint(UrlParameterConstraints<BigDecimal> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(BigDecimal value) {
            return value.compareTo(BigDecimal.valueOf(constraints.getMin())) < 0;
        }

        @Override
        public boolean triggerLengthConstraint(BigDecimal value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(BigDecimal value) {
            return value.stripTrailingZeros().scale() > constraints.getPrecision();
        }

        @Override
        public boolean triggerMaxConstraint(BigDecimal value) {
            return value.compareTo(BigDecimal.valueOf(constraints.getMax())) > 0;
        }
    }

    static class ComputeEverythingConstraint extends ComputeConstraint<Object> {
        public ComputeEverythingConstraint(UrlParameterConstraints<Object> constraints) {
            super(constraints);
        }

        @Override
        public boolean triggerMinConstraint(Object value) {
            return value.toString().length() < constraints.getMin();
        }

        @Override
        public boolean triggerLengthConstraint(Object value) {
            return value.toString().length() == constraints.getLength();
        }

        @Override
        public boolean triggerPrecisionConstraint(Object value) {
            return false;
        }

        @Override
        public boolean triggerMaxConstraint(Object value) {
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
