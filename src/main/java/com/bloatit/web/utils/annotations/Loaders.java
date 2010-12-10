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

public class Loaders {

    public static <T> String toStr(final T obj) {
        if (obj.getClass().equals(Integer.class)) {
            return new ToInteger().toString((Integer) obj);
        } else if (obj.getClass().equals(Byte.class)) {
            return new ToByte().toString((Byte) obj);
        } else if (obj.getClass().equals(Short.class)) {
            return new ToShort().toString((Short) obj);
        } else if (obj.getClass().equals(Long.class)) {
            return new ToLong().toString((Long) obj);
        } else if (obj.getClass().equals(Float.class)) {
            return new ToFloat().toString((Float) obj);
        } else if (obj.getClass().equals(Double.class)) {
            return new ToDouble().toString((Double) obj);
        } else if (obj.getClass().equals(Character.class)) {
            return new ToCharacter().toString((Character) obj);
        } else if (obj.getClass().equals(Boolean.class)) {
            return new ToBoolean().toString((Boolean) obj);
        } else if (obj.getClass().equals(BigDecimal.class)) {
            return new ToBigdecimal().toString((BigDecimal) obj);
        } else if (obj.getClass().equals(String.class)) {
            return new ToString().toString((String) obj);
        } else if (obj.getClass().equals(Date.class)) {
            return new ToDate().toString((Date) obj);
        } else if (obj.getClass().equals(Demand.class)) {
            return new ToDemand().toString((Demand) obj);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Loader<?>, U> Class<T> getLoaderClass(final Class<U> theClass) {
        if (theClass.equals(Integer.class)) {
            return Class.class.cast(ToInteger.class);
        } else if (theClass.equals(Byte.class)) {
            return Class.class.cast(ToByte.class);
        } else if (theClass.equals(Short.class)) {
            return Class.class.cast(ToShort.class);
        } else if (theClass.equals(Long.class)) {
            return Class.class.cast(ToLong.class);
        } else if (theClass.equals(Float.class)) {
            return Class.class.cast(ToFloat.class);
        } else if (theClass.equals(Double.class)) {
            return Class.class.cast(ToDouble.class);
        } else if (theClass.equals(Character.class)) {
            return Class.class.cast(ToCharacter.class);
        } else if (theClass.equals(Boolean.class)) {
            return Class.class.cast(ToBoolean.class);
        } else if (theClass.equals(BigDecimal.class)) {
            return Class.class.cast(ToBigdecimal.class);
        } else if (theClass.equals(String.class)) {
            return Class.class.cast(ToString.class);
        } else if (theClass.equals(Date.class)) {
            return Class.class.cast(ToDate.class);
        } else if (theClass.equals(Demand.class)) {
            return Class.class.cast(ToDemand.class);
        } else if (theClass.equals(Member.class)) {
            return Class.class.cast(ToMember.class);
        }
        return null;
    }

    public static class DefaultConvertor extends Loader<String> {
        @Override
        public String fromString(final String data) {
            return data;
        }
    }

    public static class ToInteger extends Loader<Integer> {
        @Override
        public Integer fromString(final String data) {
            return Integer.decode(data);
        }
    }

    public static class ToFloat extends Loader<Float> {
        @Override
        public Float fromString(final String data) {
            return Float.valueOf(data);
        }
    }

    public static class ToBigdecimal extends Loader<BigDecimal> {
        @Override
        public BigDecimal fromString(final String data) {
            return new BigDecimal(data);
        }
    }

    public static class ToByte extends Loader<Byte> {
        @Override
        public Byte fromString(final String data) {
            return Byte.valueOf(data);
        }
    }

    public static class ToShort extends Loader<Short> {
        @Override
        public Short fromString(final String data) {
            return Short.valueOf(data);
        }
    }

    public static class ToLong extends Loader<Long> {
        @Override
        public Long fromString(final String data) {
            return Long.valueOf(data);
        }
    }

    public static class ToDouble extends Loader<Double> {
        @Override
        public Double fromString(final String data) {
            return Double.valueOf(data);
        }
    }

    public static class ToCharacter extends Loader<Character> {
        @Override
        public Character fromString(final String data) {
            return data.charAt(0);
        }
    }

    public static class ToBoolean extends Loader<Boolean> {
        @Override
        public Boolean fromString(final String data) {
            return Boolean.valueOf(data);
        }
    }

    public static class ToString extends Loader<String> {
        @Override
        public String fromString(final String data) {
            return data;
        }
    }

    public static class ToDate extends Loader<Date> {
        @Override
        public Date fromString(final String data) {
            try {
                return DateFormat.getInstance().parse(data);
            } catch (final ParseException e) {
                throw new NumberFormatException();
            }
        }
    }

    public static abstract class ToIdentifiable extends Loader<Identifiable> {
        @Override
        public String toString(final Identifiable id) {
            return new Integer(id.getId()).toString();
        }
    }

    public static class ToDemand extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return DemandManager.getDemandById(Integer.valueOf(data));
        }
    }

    public static class ToMember extends ToIdentifiable {
        @Override
        public Identifiable fromString(final String data) {
            return MemberManager.getMemberById(Integer.valueOf(data));
        }
    }
}
