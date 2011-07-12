package com.bloatit.framework.webprocessor.context;



public interface UserToken {

    public abstract User getMember();
    
    public abstract boolean isAuthenticated();
}
