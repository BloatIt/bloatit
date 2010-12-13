package com.bloatit.web.utils.annotations;

public abstract class Loader<T> {

    public abstract T fromString(String data);

    public String toString(final T data) {
        return data.toString();
    }

}
