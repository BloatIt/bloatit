package com.bloatit.web.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public abstract class Loader<T> {
    
    public abstract T convert(String data);
    
    @SuppressWarnings("unchecked")
    public static <T extends Loader<?>, U> Class<T> getLoaderClass(Class<U> theClass) {
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
        }
        return null;
    }
    
    public static class DefaultConvertor extends Loader<String> {
        @Override
        public String convert(String data) {
            return data;
        }
    }

    public static class ToInteger extends Loader<Integer> {
        @Override
        public Integer convert(String data) {
            return Integer.decode(data);
        }
    }

    public static class ToFloat extends Loader<Float> {
        @Override
        public Float convert(String data) {
            return Float.valueOf(data);
        }
    }

    public static class ToBigdecimal extends Loader<BigDecimal> {
        @Override
        public BigDecimal convert(String data) {
            return new BigDecimal(data);
        }
    }

    public static class ToByte extends Loader<Byte> {
        @Override
        public Byte convert(String data) {
            return Byte.valueOf(data);
        }
    }

    public static class ToShort extends Loader<Short> {
        @Override
        public Short convert(String data) {
            return Short.valueOf(data);
        }
    }

    public static class ToLong extends Loader<Long> {
        @Override
        public Long convert(String data) {
            return Long.valueOf(data);
        }
    }

    public static class ToDouble extends Loader<Double> {
        @Override
        public Double convert(String data) {
            return Double.valueOf(data);
        }
    }

    public static class ToCharacter extends Loader<Character> {
        @Override
        public Character convert(String data) {
            return data.charAt(0);
        }
    }

    public static class ToBoolean extends Loader<Boolean> {
        @Override
        public Boolean convert(String data) {
            return Boolean.valueOf(data);
        }
    }

    public static class ToString extends Loader<String> {
        @Override
        public String convert(String data) {
            return data;
        }
    }

    public static class ToDate extends Loader<Date> {
        @Override
        public Date convert(String data) {
            try {
                return DateFormat.getInstance().parse(data);
            } catch (ParseException e) {
                throw new NumberFormatException();
            }
        }
    }
}
