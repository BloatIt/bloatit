package com.bloatit.web.utils;


public class Message {
    public enum Level {
        ERROR, WARNING, INFO
    }

    public enum What {
        NOT_FOUND, CONVERSION_ERROR
    }

    private Level level;
    private String Message;
    private What what;

    public Message(Level level, What what, String message) {
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
