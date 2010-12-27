package com.bloatit.web.utils.annotations;

import java.util.ArrayList;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.RequestParam;

/**
 * Store some error messages that could append during the reflexive procedure "setValues".
 */
public class Messages extends ArrayList<Message> {
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

    public void addError(final RequestParam param, final Message.What what, final String error) {
        this.add(new Message(param.level(), what, error));
    }

}
