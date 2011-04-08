package com.bloatit.web.pages.master;

import com.bloatit.framework.webprocessor.ErrorMessage.Level;
import com.bloatit.framework.webprocessor.components.HtmlDiv;

public final class HtmlNotification extends HtmlDiv {

    public HtmlNotification(final Level level, final String message) {
        super();
        switch (level) {
            case INFO:
                setCssClass("notification_good");
                break;
            case WARNING:
                setCssClass("notification_bad");
                break;
            case FATAL:
                setCssClass("notification_error");
                break;
            default:
                // Do nothing
                break;
        }
        addText(message);
    }

}
