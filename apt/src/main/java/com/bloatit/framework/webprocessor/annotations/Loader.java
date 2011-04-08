package com.bloatit.framework.webprocessor.annotations;

public abstract class Loader<T> {

    public abstract T fromString(String data) throws ConversionErrorException;

    public String toString(final T data) {
        return data.toString();
    }

    public static class DefaultConvertor extends Loader<String> {
        @Override
        public String fromString(final String data) {
            return data;
        }
    }
}
