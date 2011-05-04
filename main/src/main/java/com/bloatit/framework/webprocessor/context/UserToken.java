package com.bloatit.framework.webprocessor.context;

import java.util.UUID;


public interface UserToken {

    /**
     * @return a unique key, identifying this authToken.
     */
    public abstract UUID getKey();

    public abstract User getMember();
    
    public abstract boolean isAuthenticated();
}
