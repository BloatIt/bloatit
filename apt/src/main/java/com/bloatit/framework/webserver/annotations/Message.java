package com.bloatit.framework.webserver.annotations;

public class Message {
    public enum Level {
        ERROR, WARNING, INFO
    }

    public enum What {
        UNKNOWN, NOT_FOUND, CONVERSION_ERROR, MIN_ERROR, MAX_ERROR, NO_ERROR, LENGTH_ERROR, OPTIONAL_ERROR, PRECISION_ERROR
    }

    private final Level level;
    private final String message;
    private final What what;

    public Message(final String message, final Level level, final What what, final String name, final String value) {
        super();
        if (level == null || what == null || message == null) {
            throw new NullPointerException();
        }
        this.level = level;
        this.message = extractErrorMessage(message, name, value);
        this.what = what;
    }

    public Message(final String message, final Level level){
        this.level = level;
        this.message = message;
        this.what = What.UNKNOWN;
    }

    private String extractErrorMessage(String aMessage, String name, String value) {
        String errorMsg = aMessage.replaceAll("%param", name);
        if (!value.isEmpty()) {
            errorMsg = errorMsg.replaceAll("%value", value);
        } else {
            errorMsg = errorMsg.replaceAll("%value", "null");
        }
        return errorMsg;
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
