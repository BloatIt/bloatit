package com.bloatit.model;


public class AnonymousUserToken implements ElveosUserToken {

    public AnonymousUserToken() {
        super();
    }

    @Override
    public Team getAsTeam() {
        return null;
    }

    @Override
    public void setAsTeam(final Team team) {
        // Do nothing ...
    }

    @Override
    public Member getMember() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

}
