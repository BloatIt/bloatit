package test;

import test.html.components.basicComponents.HtmlBlock;

public class Notification extends HtmlBlock {

    public enum Level {
        INFO, WARNING, ERROR
    }

    public Notification(Level level, String message) {
        super();
        switch (level) {
        case INFO:
            setClass("notification_good");
            break;
        case WARNING:
            setClass("notification_bad");
            break;
        case ERROR:
            setClass("notification_error");
            break;
        }
        addText(message);
    }

}
