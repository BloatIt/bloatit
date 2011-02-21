package com.bloatit.framework.webserver.url;

import java.util.ArrayList;

import com.bloatit.framework.webserver.annotations.Message;

/**
 * Store some error messages that could append during the reflexive procedure
 * "setValues".
 */
public final class Messages extends ArrayList<Message> {
    private static final long serialVersionUID = -7080211414458545384L;

    public Messages() {
        super();
    }

    public boolean hasMessage(final Message.Level level) {
        for (final Message error : this) {
            if (error.getLevel() == level) {
                return true;
            }
        }
        return false;
    }
}
