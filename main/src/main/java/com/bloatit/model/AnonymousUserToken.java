package com.bloatit.model;

import java.util.UUID;

public class AnonymousUserToken implements ElveosUserToken {

    public AnonymousUserToken() {
        super();
    }

    @Override
    public Team getAsTeam() {
        return null;
    }

    @Override
    public void setAsTeam(Team team) {
        // Do nothing ...
    }

    @Override
    public UUID getKey() {
        return null;
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
