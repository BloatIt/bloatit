package com.bloatit.web.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryParam {

    public static interface FromString<T> {
        public T convert(String data);
    }

    public static class DefaultConvertor implements FromString<String> {
        @Override
        public String convert(String data) {
            return data;
        }
    }

    String name() default "";

    String error() default "Ceci est une erreur";

    Class<? extends FromString<?>> loader() default DefaultConvertor.class;

    String defaultValue() default "";

}
