package com.bloatit.model;

import java.util.Date;

import com.bloatit.framework.right.ActorRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoActor;

public abstract class Actor extends Identifiable {

    protected abstract DaoActor getDaoActor();

    protected int getDaoId() {
        return getDaoActor().getId();
    }

    public boolean canGetEmail() {
        return new ActorRight.Email().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public String getEmail() {
        new ActorRight.Email().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return getDaoActor().getEmail();
    }

    public void setEmail(String email) {
        new ActorRight.Email().tryAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
        getDaoActor().setEmail(email);
    }

    protected String getUnprotectedLogin() {
        return getDaoActor().getLogin();
    }

    public String getLogin() {
        return getUnprotectedLogin();
    }

    public Date getDateCreation() {
        return getDaoActor().getDateCreation();
    }

    public boolean canGetInternalAccount() {
        return new ActorRight.InternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public InternalAccount getInternalAccount() {
        new ActorRight.InternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return new InternalAccount(getDaoActor().getInternalAccount());
    }

    public boolean canGetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public ExternalAccount getExternalAccount() {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return new ExternalAccount(getDaoActor().getExternalAccount());
    }

    public boolean canSetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
    }

    public void setExternalAccount(ExternalAccount externalAccount) {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
        getDaoActor().setExternalAccount(externalAccount.getDao());
    }
}
