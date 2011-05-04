package com.bloatit.framework.webprocessor.context;

import java.util.UUID;


public interface AbstractAuthToken {

    /**
     * @return a unique key, identifying this authToken.
     */
    public abstract UUID getKey();

    public abstract User getMember();
}
