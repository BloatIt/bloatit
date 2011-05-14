package com.bloatit.model;

import com.bloatit.framework.webprocessor.context.UserToken;

public interface ElveosUserToken extends UserToken {

    public abstract Team getAsTeam();

    public abstract void setAsTeam(final Team team);

    @Override
    public abstract Member getMember();

}
