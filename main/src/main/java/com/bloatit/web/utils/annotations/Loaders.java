package com.bloatit.web.utils.annotations;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.bloatit.framework.Demand;
import com.bloatit.framework.Identifiable;
import com.bloatit.framework.Member;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.annotations.Loader;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.BloatitDate;
import com.bloatit.web.utils.DateParsingException;

public class Loaders {

    public static <T> String toStr(final T obj) throws ConversionErrorException {
        if (obj == null) {
            return "null";
        }
        @SuppressWarnings("unchecked")
        final Loader<T> loader = (Loader<T>) getLoader(obj.getClass());
        try {
            return loader.toString(obj);
        } catch (final Exception e) {
            throw new ConversionErrorException("Cannot convert " + obj + " to String.");
        }
    }

    public static <T> T fromStr(final Class<T> toClass, final String value) throws ConversionErrorException {
        if (value == "null") {
            return null;
        }
        final Loader<T> loader = (Loader<T>) getLoader(toClass);
        try {
            return loader.fromString(value);
        } catch (final Exception e) {
            throw new ConversionErrorException("Cannot convert " + value + " to " + toClass.toString());
        }
    }

    @SuppressWarnings("unchecked")
    static <T> Loader<T> getLoader(final Class<T> theClass) {
        if (theClass.equals(Integer.class)) {
            return (Loader<T>) new ToInteger();
        } else if (theClass.equals(Byte.class)) {
            return (Loader<T>) new ToByte();
        } else if (theClass.equals(Short.class)) {
            return (Loader<T>) new ToShort();
        } else if (theClass.equals(Long.class)) {
            return (Loader<T>) new ToLong();
        } else if (theClass.equals(Float.class)) {
            return (Loader<T>) new ToFloat();
        } else if (theClass.equals(Double.class)) {
            return (Loader<T>) new ToDouble();
        } else if (theClass.equals(Character.class)) {
            return (Loader<T>) new ToCharacter();
        } else if (theClass.equals(Boolean.class)) {
            return (Loader<T>) new ToBoolean();
        } else if (theClass.equals(BigDecimal.class)) {
            return (Loader<T>) new ToBigdecimal();
        } else if (theClass.equals(String.class)) {
            return (Loader<T>) new ToString();
        } else if (theClass.equals(Date.class)) {
            return (Loader<T>) new ToDate();
        } else if (theClass.equals(Demand.class)) {
            return (Loader<T>) new ToDemand();
        } else if (theClass.equals(Member.class)) {
            return (Loader<T>) new ToMember();
        } else if (theClass.equals(BloatitDate.class)) {
            return (Loader<T>) new ToBloatitDate();
        }
        return null;
    }
    
    /**
     * This exception is thrown when a parameter is not found in the map.
     */
    public static class ParamNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParamNotFoundException(final String message) {
            super(message);
        }
    }

    /**
     * This exception is thrown when a parameter is found, but cannot be
     * converted to the
     * right type.
     */
    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(final String message) {
            super(message);
        }
    }

    private static class ToInteger extends Loader<Integer> {
        @Override
        public Integer fromString(final String data) {
            return Integer.decode(data);
        }
    }

    private static class ToFloat extends Loader<Float> {
        @Override
        public Float fromString(final String data) {
            return Float.valueOf(data);
        }
    }

    private static class ToBigdecimal extends Loader<BigDecimal> {
        @Override
        public BigDecimal fromString(final String data) {
            return new BigDecimal(data);
        }
    }

    private static class ToByte extends Loader<Byte> {
        @Override
        public Byte fromString(final String data) {
            return Byte.valueOf(data);
        }
    }

    private static class ToShort extends Loader<Short> {
        @Override
        public Short fromString(final String data) {
            return Short.valueOf(data);
        }
    }

    private static class ToLong extends Loader<Long> {
        @Override
        public Long fromString(final String data) {
            return Long.valueOf(data);
        }
    }

    private static class ToDouble extends Loader<Double> {
        @Override
        public Double fromString(final String data) {
            return Double.valueOf(data);
        }
    }

    private static class ToCharacter extends Loader<Character> {
        @Override
        public Character fromString(final String data) {
            return data.charAt(0);
        }
    }

    private static class ToBoolean extends Loader<Boolean> {
        @Override
        public Boolean fromString(final String data) {
            return Boolean.valueOf(data);
        }
    }

    private static class ToString extends Loader<String> {
        @Override
        public String fromString(final String data) {
            return data;
        }
    }

    private static class ToDate extends Loader<Date> {
        @Override
        public Date fromString(final String data) {
            try {
                return DateFormat.getInstance().parse(data);
            } catch (final ParseException e) {
                throw new NumberFormatException();
            }
        }
    }

    private static class ToBloatitDate extends Loader<BloatitDate> {
        @Override
        public BloatitDate fromString(final String data) {
            try {
                return new BloatitDate(data, Context.getSession().getLanguage().getLocale());
            } catch (DateParsingException e) {
                throw new NumberFormatException();
            }
        }
    }

    private static abstract class ToIdentifiable extends Loader<Identifiable> {
        @Override
        public String toString(final Identifiable id) {
            return new Integer(id.getId()).toString();
        }
    }

    private static class ToDemand extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return DemandManager.getDemandById(Integer.valueOf(data));
        }
    }

    private static class ToMember extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return MemberManager.getMemberById(Integer.valueOf(data));
        }
    }
}
