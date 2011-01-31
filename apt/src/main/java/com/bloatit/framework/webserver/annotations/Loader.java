package com.bloatit.framework.webserver.annotations;

public abstract class Loader<T> {

    public abstract T fromString(String data);

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
