package com.bloatit.framework.webprocessor.url.constraints;


import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.annotations.MessageFormater;

public abstract class Constraint<T> {

    private final String message;

    public Constraint(final String message) {
        this.message = message;
    }

    public Message getMessage(final T value, final MessageFormater formater) {
        if (verify(value)) {
            return null;
        }
        updateFormater(formater, value);
        return new Message(message, formater);
    }

    public abstract boolean verify(T value);

    protected abstract void updateFormater(MessageFormater formater, T value);

    static boolean cmp(final boolean inferior, final boolean exclusive, final int value) {
        if (inferior && exclusive) {
            return value < 0;
        }
        if (inferior && !exclusive) {
            return value <= 0;
        }
        if (!inferior && exclusive) {
            return value > 0;
        }
        if (!inferior && !exclusive) {
            return value >= 0;
        }
        throw new BadProgrammerException("I cannot be !");
    }
}
