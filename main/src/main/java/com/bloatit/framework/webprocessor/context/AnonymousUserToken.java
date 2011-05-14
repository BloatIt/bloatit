package com.bloatit.framework.webprocessor.context;

import java.util.UUID;

class AnonymousUserToken implements UserToken{

    protected static final AnonymousUserToken TOKEN = new AnonymousUserToken();
    
    @Override
    public UUID getKey() {
        return null;
    }

    @Override
    public User getMember() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

}
