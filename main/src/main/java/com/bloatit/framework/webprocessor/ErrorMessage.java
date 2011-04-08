package com.bloatit.framework.webprocessor;

public class ErrorMessage {
    public enum Level {
        INFO, WARNING, FATAL
    }

    private final Level level;
    private final String message;

    public ErrorMessage(final Level level, final String message) {
        super();
        this.level = level;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Level getLevel() {
        return level;
    }
}
