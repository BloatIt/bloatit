//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.url;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.NotImplementedException;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.utils.i18n.DateParsingException;
import com.bloatit.framework.webprocessor.annotations.ConversionErrorException;
import com.bloatit.framework.webprocessor.annotations.Loader;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Commentable;
import com.bloatit.model.GenericConstructor;
import com.bloatit.model.Identifiable;
import com.bloatit.model.IdentifiableInterface;
import com.bloatit.model.managers.CommentManager;
import com.bloatit.web.linkable.master.WebProcess;

public final class Loaders {

    /**
     * desactivate ctor
     */
    private Loaders() {
        // desactivate ctor
    }

    protected static <T> String toStr(final T obj) {
        if (obj == null) {
            return "";
        }
        @SuppressWarnings("unchecked") final Loader<T> loader = (Loader<T>) getLoader(obj.getClass());
        return loader.toString(obj);
    }

    public static <T> T fromStr(final Class<T> toClass, final String value) throws ConversionErrorException {
        // if (value.equals("null")) {
        // throw new NotImplementedException();
        // }
        try {
            final Loader<T> loader = getLoader(toClass);
            return loader.fromString(value);
        } catch (final ConversionErrorException e) {
            throw e;
        }
    }

    private @SuppressWarnings({ "unchecked", "synthetic-access", "rawtypes" })
    static <T> Loader<T> getLoader(final Class<T> theClass) {
        if (theClass.equals(Integer.class)) {
            return (Loader<T>) new ToInteger();
        }
        if (theClass.equals(Byte.class)) {
            return (Loader<T>) new ToByte();
        }
        if (theClass.isEnum()) {
            return new ToEnum(theClass);
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
        if (theClass.equals(Locale.class)) {
            return (Loader<T>) new ToLocale();
        }
        if (theClass.equals(Date.class)) {
            return (Loader<T>) new ToDate();
        }
        if (Commentable.class.equals(theClass)) {
            return (Loader<T>) new ToCommentable();
        }
        if (theClass.equals(DateLocale.class)) {
            return (Loader<T>) new ToBloatitDate();
        }
        if (IdentifiableInterface.class.isAssignableFrom(theClass)) {
            return new ToIdentifiable(theClass);
        }
        if (WebProcess.class.isAssignableFrom(theClass)) {
            return new ToWebProcess();
        }
        throw new NotImplementedException("Cannot find a conversion class for: " + theClass);
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
            return Character.valueOf(data.charAt(0));
        }
    }

    private static class ToBoolean extends Loader<Boolean> {
        @Override
        public Boolean fromString(final String data) {
            try {
                return data != null && !data.equals("off") && !data.equals("OFF") && !data.equals("FALSE") && !data.equals("false");
            } catch (final NumberFormatException e) {
                return true;
            }
        }
    }

    private static class ToString extends Loader<String> {
        @Override
        public String fromString(final String data) {
            return data;
        }
    }

    private static class ToLocale extends Loader<Locale> {
        @Override
        public Locale fromString(final String data) {
            return new Locale(data);
        }

        @Override
        public String toString(final Locale locale) {
            return locale.getLanguage();
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

        @Override
        public String toString(final DateLocale date) {
            return date.getIsoDateString();
        }
    }

    private static class ToCommentable extends Loader<Commentable> {
        @Override
        public String toString(final Commentable data) {
            return data.getId().toString();
        }

        @Override
        public Commentable fromString(final String data) throws ConversionErrorException {
            try {
                Commentable commentable = CommentManager.getCommentable(Integer.valueOf(data));
                if (commentable != null) {
                    return commentable;
                }
                throw new ConversionErrorException("Commentable not found for Id: " + data);
            } catch (final NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToIdentifiable<T extends Identifiable<?>> extends Loader<T> {

        private final Class<T> theClass;

        public ToIdentifiable(final Class<T> theClass) {
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
            } catch (final ClassCastException e) {
                throw new ConversionErrorException(e);
            } catch (final ClassNotFoundException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToWebProcess<T extends WebProcess> extends Loader<T> {

        @Override
        public final String toString(final T id) {
            return id.getId().toString();
        }

        @SuppressWarnings("unchecked")
        @Override
        public final T fromString(final String data) throws ConversionErrorException {
            final WebProcess webProcess = Context.getSession().getWebProcess(data);
            if (webProcess != null) {
                webProcess.load();
            }
            return (T) webProcess;

        }
    }

}
