package com.bloatit.web.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bloatit.common.FatalErrorException;
import com.bloatit.web.utils.QueryParam.FromString;

public class QueryParamProcessor {

    private List<String> errors = new ArrayList<String>();

    public void parse(Object instance, Map<String, String> parameters) {
        Class<?> aClass = instance.getClass();
        for (Field f : aClass.getDeclaredFields()) {
            QueryParam param = f.getAnnotation(QueryParam.class);
            if (param != null) {

                //
                // First get the parameter from the parameters map.
                //

                String value;
                if (param.name().isEmpty()) {
                    value = parameters.get(f.getName());
                } else {
                    value = parameters.get(param.name());
                }

                if (value == null && (value = param.defaultValue()).isEmpty()) {
                    // Value not found stop here.
                    errors.add(param.error());
                    return;
                }

                //
                // Get the loader class that translate from string to ...
                //

                Class<? extends QueryParam.FromString<?>> valueClass = null;
                if (param.loader().equals(QueryParam.DefaultConvertor.class)) {
                    // default one.
                    valueClass = findConversionType(f.getType());
                } else {
                    valueClass = param.loader();
                }

                // if the loader is not found there is a fatal error !
                // It can only arrive if there is a big programming error.
                if (valueClass == null) {
                    throw new FatalErrorException("Loader class not found.", null);
                }

                try {
                    if (!f.isAccessible()) {
                        f.setAccessible(true);
                    }
                    Constructor<? extends FromString<?>> constructor = valueClass.getConstructor();
                    FromString<?> newInstance = constructor.newInstance();
                    Object convert = newInstance.convert(value);
                    
                    f.set(instance, convert);
                } catch (Exception e) {
                    errors.add(param.error());
                }
            }
        }
    }

    public static class ToInteger implements QueryParam.FromString<Integer> {
        @Override
        public Integer convert(String data) {
            return Integer.decode(data);
        }
    }

    public static class ToFloat implements QueryParam.FromString<Float> {
        @Override
        public Float convert(String data) {
            return Float.valueOf(data);
        }
    }

    public static class ToBigdecimal implements QueryParam.FromString<BigDecimal> {
        @Override
        public BigDecimal convert(String data) {
            return new BigDecimal(data);
        }
    }

    public static class ToByte implements QueryParam.FromString<Byte> {
        @Override
        public Byte convert(String data) {
            return Byte.valueOf(data);
        }
    }

    public static class ToShort implements QueryParam.FromString<Short> {
        @Override
        public Short convert(String data) {
            return Short.valueOf(data);
        }
    }

    public static class ToLong implements QueryParam.FromString<Long> {
        @Override
        public Long convert(String data) {
            return Long.valueOf(data);
        }
    }

    public static class ToDouble implements QueryParam.FromString<Double> {
        @Override
        public Double convert(String data) {
            return Double.valueOf(data);
        }
    }

    public static class ToCharacter implements QueryParam.FromString<Character> {
        @Override
        public Character convert(String data) {
            return data.charAt(0);
        }
    }

    public static class ToBoolean implements QueryParam.FromString<Boolean> {
        @Override
        public Boolean convert(String data) {
            return Boolean.valueOf(data);
        }
    }

    public static class ToString implements QueryParam.FromString<String> {
        @Override
        public String convert(String data) {
            return data;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends QueryParam.FromString<?>, U> Class<T> findConversionType(Class<U> theClass) {
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
        }
        return null;
    }

    public List<String> getErrors() {
        return errors;
    }
    
}
