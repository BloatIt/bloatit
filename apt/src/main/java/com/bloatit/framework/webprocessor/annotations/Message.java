package com.bloatit.framework.webprocessor.annotations;

public final class Message {
    private String message;
    private MessageFormater formater;

    public Message(final String message, final MessageFormater formater) {
        this(message);
        this.formater = formater;
    }

    public Message(final String message) {
        super();
        this.message = message;
    }

    public String getMessage(Translator translator) {
        if (formater != null) {
            return formater.format(translator.tr(message));
        } else {
            return translator.tr(message);
        }

    }
}
