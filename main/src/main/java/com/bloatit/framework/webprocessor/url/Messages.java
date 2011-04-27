/*
 * 
 */
package com.bloatit.framework.webprocessor.url;

import java.util.ArrayList;

import com.bloatit.framework.webprocessor.annotations.Message;

/**
 * Store some error messages that could append during the reflexive procedure
 * "setValues".
 */
public final class Messages extends ArrayList<Message> {
    private static final long serialVersionUID = -7080211414458545384L;

    public Messages() {
        super();
    }

    public boolean hasMessage() {
        return !isEmpty();
    }
}
