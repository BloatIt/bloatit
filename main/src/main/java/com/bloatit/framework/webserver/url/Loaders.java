package com.bloatit.framework.webserver.url;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.bloatit.framework.utils.i18n.DateLocale;
import com.bloatit.framework.utils.i18n.DateParsingException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.framework.webserver.annotations.Loader;
import com.bloatit.model.Comment;
import com.bloatit.model.Demand;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Identifiable;
import com.bloatit.model.Kudosable;
import com.bloatit.model.Member;
import com.bloatit.model.Project;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.managers.CommentManager;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.managers.KudosableManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.ProjectManager;

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
        @SuppressWarnings("unchecked")
        final Loader<T> loader = (Loader<T>) getLoader(obj.getClass());
        return loader.toString(obj);
    }

    public static <T> T fromStr(final Class<T> toClass, final String value) throws ConversionErrorException {
        if (value.equals("null")) {
            return null;
        }
        final Loader<T> loader = getLoader(toClass);
        return loader.fromString(value);
    }

    @SuppressWarnings({ "unchecked", "synthetic-access" })
    static <T> Loader<T> getLoader(final Class<T> theClass) throws ConversionErrorException {
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
        } else if (theClass.equals(DemandImplementation.class) || theClass.equals(Demand.class)) {
            return (Loader<T>) new ToDemand();
        } else if (theClass.equals(Member.class)) {
            return (Loader<T>) new ToMember();
        } else if (theClass.equals(DateLocale.class)) {
            return (Loader<T>) new ToBloatitDate();
        } else if (theClass.equals(Kudosable.class)) {
            return (Loader<T>) new ToKudosable();
        } else if (theClass.equals(Comment.class)) {
            return (Loader<T>) new ToComment();
        } else if (theClass.equals(Project.class)) {
            return (Loader<T>) new ToProject();
        } else if (theClass.equals(FileMetadata.class)) {
            return (Loader<T>) new ToFileMetadata();
        }
        throw new ConversionErrorException("Cannot find a convertion class for: " + theClass);
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
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToFloat extends Loader<Float> {
        @Override
        public Float fromString(final String data) throws ConversionErrorException {
            try {
                return Float.valueOf(data);
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToBigdecimal extends Loader<BigDecimal> {
        @Override
        public BigDecimal fromString(final String data) throws ConversionErrorException {
            try {
                return new BigDecimal(data);
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToByte extends Loader<Byte> {
        @Override
        public Byte fromString(final String data) throws ConversionErrorException {
            try {
                return Byte.valueOf(data);
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToShort extends Loader<Short> {
        @Override
        public Short fromString(final String data) throws ConversionErrorException {
            try {
                return Short.valueOf(data);
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToLong extends Loader<Long> {
        @Override
        public Long fromString(final String data) throws ConversionErrorException {
            try {
                return Long.valueOf(data);
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }
    }

    private static class ToDouble extends Loader<Double> {
        @Override
        public Double fromString(final String data) throws ConversionErrorException {
            try {
                return Double.valueOf(data);
            } catch (NumberFormatException e) {
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

    private abstract static class ToIdentifiable extends Loader<Identifiable<?>> {
        @Override
        public final String toString(final Identifiable<?> id) {
            return String.valueOf(id.getId());
        }

        @Override
        public final Identifiable<?> fromString(final String data) throws ConversionErrorException {
            try {
                Identifiable<?> fromStr = doFromString(Integer.valueOf(data));
                if (fromStr == null) {
                    throw new ConversionErrorException("Identifiable not found for Id: " + data);
                }
                return fromStr;
            } catch (NumberFormatException e) {
                throw new ConversionErrorException(e);
            }
        }

        public abstract Identifiable<?> doFromString(int i);
    }

    private static class ToDemand extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return (Identifiable<?>) DemandManager.getDemandById(i);
        }
    }

    private static class ToMember extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return MemberManager.getMemberById(i);
        }
    }

    private static class ToProject extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return ProjectManager.getProjectById(i);
        }
    }

    private static class ToFileMetadata extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return FileMetadataManager.getFileMetadataById(i);
        }
    }

    private static class ToKudosable extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return KudosableManager.getById(i);
        }
    }

    private static class ToComment extends ToIdentifiable {
        @Override
        public Identifiable<?> doFromString(int i) {
            return CommentManager.getCommentById(i);
        }
    }
}
