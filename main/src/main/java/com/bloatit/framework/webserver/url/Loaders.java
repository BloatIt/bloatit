package com.bloatit.framework.webserver.url;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.common.Log;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.data.queries.DaoIdentifiableQuery;
import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.utils.i18n.DateParsingException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.framework.webserver.annotations.Loader;
import com.bloatit.model.Commentable;
import com.bloatit.model.DataVisitorConstructor;
import com.bloatit.model.GenericConstructor;
import com.bloatit.model.Identifiable;
import com.bloatit.model.managers.GenericManager;

public final class Loaders {

    /**
     * desactivate ctor
     */
    private Loaders() {
        // desactivate ctor
    }

    public static <T> String toStr(final T obj) throws ConversionErrorException {
        if (obj == null) {
            return "";
        }
        try {
            @SuppressWarnings("unchecked") final Loader<T> loader = (Loader<T>) getLoader(obj.getClass());
            return loader.toString(obj);
        } catch (ConversionErrorException e) {
            Log.framework().debug("Conversion error in toStr.", e);
            throw e;
        }
    }

    public static <T> T fromStr(final Class<T> toClass, final String value) throws ConversionErrorException {
        // if (value.equals("null")) {
        // return null;
        // }
        try {
            final Loader<T> loader = getLoader(toClass);
            return loader.fromString(value);
        } catch (ConversionErrorException e) {
            Log.framework().debug("Conversion error in fromStr.", e);
            throw e;
        }
    }

    @SuppressWarnings({ "unchecked", "synthetic-access", "cast", "rawtypes" })
    static <T> Loader<T> getLoader(final Class<T> theClass) throws ConversionErrorException {
        if (theClass.equals(Integer.class)) {
            return (Loader<T>) new ToInteger();
        }
        if (theClass.equals(Byte.class)) {
            return (Loader<T>) new ToByte();
        }
        if (theClass.isEnum()) {
            return (Loader<T>) new ToEnum(theClass);
        }
        if (theClass.equals(Short.class)) {
            return (Loader<T>) new ToShort();
        }
        if (theClass.equals(Long.class)) {
            return (Loader<T>) new ToLong();
        }
        if (theClass.equals(Float.class)) {
            return (Loader<T>) new ToFloat();
        }
        if (theClass.equals(Double.class)) {
            return (Loader<T>) new ToDouble();
        }
        if (theClass.equals(Character.class)) {
            return (Loader<T>) new ToCharacter();
        }
        if (theClass.equals(Boolean.class)) {
            return (Loader<T>) new ToBoolean();
        }
        if (theClass.equals(BigDecimal.class)) {
            return (Loader<T>) new ToBigdecimal();
        }
        if (theClass.equals(String.class)) {
            return (Loader<T>) new ToString();
        }
        if (theClass.equals(Date.class)) {
            return (Loader<T>) new ToDate();
        }
        if (Commentable.class.equals(theClass)) {
            return (Loader<T>) new LowToIdentifiable();
        }
        if (theClass.equals(DateLocale.class)) {
            return (Loader<T>) new ToBloatitDate();
        }
        if (IdentifiableInterface.class.isAssignableFrom(theClass)) {
            return (Loader<T>) new ToIdentifiable(theClass);
        }
        throw new NotImplementedException("Cannot find a convertion class for: " + theClass);
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

    private static class ToInteger extends Loader<Integer> {
        @Override
        public Integer fromString(final String data) throws ConversionErrorException {
            try {
                return Integer.decode(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToEnum<T extends Enum<T>> extends Loader<Enum<T>> {
        private final Class<T> type;

        public ToEnum(final Class<T> type) {
            super();
            this.type = type;
        }

        @Override
        public String toString(final Enum<T> data) {
            return data.name();
        }

        @Override
        public Enum<T> fromString(final String data) throws ConversionErrorException {
            try {
                return Enum.valueOf(type, data);
            } catch (final IllegalArgumentException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToFloat extends Loader<Float> {
        @Override
        public Float fromString(final String data) throws ConversionErrorException {
            try {
                return Float.valueOf(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToBigdecimal extends Loader<BigDecimal> {
        @Override
        public BigDecimal fromString(final String data) throws ConversionErrorException {
            try {
                return new BigDecimal(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToByte extends Loader<Byte> {
        @Override
        public Byte fromString(final String data) throws ConversionErrorException {
            try {
                return Byte.valueOf(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToShort extends Loader<Short> {
        @Override
        public Short fromString(final String data) throws ConversionErrorException {
            try {
                return Short.valueOf(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToLong extends Loader<Long> {
        @Override
        public Long fromString(final String data) throws ConversionErrorException {
            try {
                return Long.valueOf(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToDouble extends Loader<Double> {
        @Override
        public Double fromString(final String data) throws ConversionErrorException {
            try {
                return Double.valueOf(data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
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
            return Boolean.valueOf(data) || data.equals("on");
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
        public Date fromString(final String data) throws ConversionErrorException {
            try {
                return DateFormat.getInstance().parse(data);
            } catch (final ParseException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToBloatitDate extends Loader<DateLocale> {
        @Override
        public DateLocale fromString(final String data) throws ConversionErrorException {
            try {
                return new DateLocale(data, Context.getLocalizator().getLocale());
            } catch (final DateParsingException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class LowToIdentifiable extends Loader<Identifiable<?>> {
        @Override
        public String toString(final Identifiable<?> data) {
            return data.getId().toString();
        }

        @Override
        public Identifiable<?> fromString(final String data) throws ConversionErrorException {
            try {
                DaoIdentifiableQuery daoIdentifiableQuery = new DaoIdentifiableQuery();
                daoIdentifiableQuery.idEquals(Integer.valueOf(data));
                return daoIdentifiableQuery.uniqueResult().accept(new DataVisitorConstructor());
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToIdentifiable<T extends Identifiable<?>> extends Loader<T> {

        private final Class<T> theClass;

        public ToIdentifiable(Class<T> theClass) {
            this.theClass = theClass;
        }

        @Override
        public final String toString(final T id) {
            return String.valueOf(id.getId());
        }

        @Override
        public final T fromString(final String data) throws ConversionErrorException {
            try {
                final T fromStr = theClass.cast(GenericConstructor.create(theClass, Integer.valueOf(data)));
                if (fromStr == null) {
                    throw new ConversionErrorException("Identifiable not found for Id: " + data);
                }
                return fromStr;
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            } catch (ClassCastException e) {
                throw new ConversionErrorException(e);
            } catch (ClassNotFoundException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

}
