package com.bloatit.web.utils.annotations;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.bloatit.common.FatalErrorException;
import com.bloatit.framework.Comment;
import com.bloatit.framework.Demand;
import com.bloatit.framework.Identifiable;
import com.bloatit.framework.Kudosable;
import com.bloatit.framework.Member;
import com.bloatit.framework.managers.CommentManager;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.framework.managers.KudosableManager;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.annotations.Loader;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.i18n.DateLocale;
import com.bloatit.web.utils.i18n.DateParsingException;

public final class Loaders {

    /**
     * desactivate ctor
     */
    private Loaders() {
        // desactivate ctor
    }

    public static <T> String toStr(final T obj) throws ConversionErrorException {
        if (obj == null) {
            return "null";
        }
        @SuppressWarnings("unchecked")
        final Loader<T> loader = (Loader<T>) getLoader(obj.getClass());
        try {
            return loader.toString(obj);
        } catch (final Exception e) {
            throw new ConversionErrorException("Cannot convert " + obj + " to String.", e);
        }
    }

    public static <T> T fromStr(final Class<T> toClass, final String value) throws ConversionErrorException {
        if (value.equals("null")) {
            return null;
        }
        final Loader<T> loader = getLoader(toClass);
        try {
            return loader.fromString(value);
        } catch (final Exception e) {
            throw new ConversionErrorException("Cannot convert " + value + " to " + toClass.toString(), e);
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
        } else if (theClass.equals(DateLocale.class)) {
            return (Loader<T>) new ToBloatitDate();
        } else if (theClass.equals(Kudosable.class)) {
            return (Loader<T>) new ToKudosable();
        } else if (theClass.equals(Comment.class)) {
            return (Loader<T>) new ToComment();
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
     * This exception is thrown when a parameter is found, but cannot be converted to the
     * right type.
     */
    public static class ConversionErrorException extends Exception {
        private static final long serialVersionUID = 1L;

        protected ConversionErrorException(final String message) {
            super(message);
        }

        public ConversionErrorException(final String message, final Throwable cause) {
            super(message, cause);
        }

        public ConversionErrorException(final Throwable cause) {
            super(cause);
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
                throw new FatalErrorException(e);
            }
        }
    }

    private static class ToBloatitDate extends Loader<DateLocale> {
        @Override
        public DateLocale fromString(final String data) {
            try {
                return new DateLocale(data, Context.getLocalizator().getLocale());
            } catch (final DateParsingException e) {
                throw new FatalErrorException(e);
            }
        }
    }

    private abstract static class ToIdentifiable extends Loader<Identifiable> {
        @Override
        public String toString(final Identifiable id) {
            return String.valueOf(id.getId());
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

    private static class ToKudosable extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return KudosableManager.getById(Integer.valueOf(data));
        }
    }

    private static class ToComment extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return CommentManager.getCommentById(Integer.valueOf(data));
        }
    }
}
