package com.bloatit.web.annotations;

public class Message {
    public enum Level {
        ERROR, WARNING, INFO
    }

    public enum What {
        NOT_FOUND, CONVERSION_ERROR, NO_ERROR
    }

    private final Level level;
    private final String message;
    private final What what;

    public Message(final Level level, final What what, final String message) {
        super();
        if (level == null || what == null || message == null) {
            throw new NullPointerException();
        }
        this.level = level;
        this.message = message;
        this.what = what;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public What getWhat() {
        return what;
    }
}
