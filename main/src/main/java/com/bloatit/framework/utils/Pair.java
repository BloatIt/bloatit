package com.bloatit.framework.utils;

public class Pair<T, U> {
    public final T first;
    public final U second;

    public Pair(final T first, final U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
