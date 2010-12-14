package com.bloatit.web.annotations;

public class Message {
    public enum Level {
        ERROR, WARNING, INFO
    }

    public enum What {
        NOT_FOUND, CONVERSION_ERROR
    }

    private final Level level;
    private final String Message;
    private final What what;

    public Message(final Level level, final What what, final String message) {
        super();
        this.level = level;
        Message = message;
        this.what = what;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return Message;
    }

    public What getWhat() {
        return what;
    }
}
