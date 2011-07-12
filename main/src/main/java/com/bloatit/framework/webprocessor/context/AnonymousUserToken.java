package com.bloatit.framework.webprocessor.context;


class AnonymousUserToken implements UserToken{

    protected static final AnonymousUserToken TOKEN = new AnonymousUserToken();
    
    @Override
    public User getMember() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

}
