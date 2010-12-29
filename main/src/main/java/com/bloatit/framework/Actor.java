package com.bloatit.framework;

import java.util.Date;

import com.bloatit.framework.right.ActorRight;
import com.bloatit.framework.right.RightManager.Action;
import com.bloatit.model.data.DaoActor;

public abstract class Actor extends Identifiable {

    protected abstract DaoActor getDaoActor();

    @Override
    public final int getId() {
        return getDaoActor().getId();
    }

    public final boolean canGetEmail() {
        return new ActorRight.Email().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public final String getEmail() {
        new ActorRight.Email().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return getDaoActor().getEmail();
    }

    public final void setEmail(final String email) {
        new ActorRight.Email().tryAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
        getDaoActor().setEmail(email);
    }

    protected final String getUnprotectedLogin() {
        return getDaoActor().getLogin();
    }

    public final String getLogin() {
        return getUnprotectedLogin();
    }

    public final Date getDateCreation() {
        return getDaoActor().getDateCreation();
    }

    public final boolean canGetInternalAccount() {
        return new ActorRight.InternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public final InternalAccount getInternalAccount() {
        new ActorRight.InternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return new InternalAccount(getDaoActor().getInternalAccount());
    }

    public final boolean canGetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.READ);
    }

    public final ExternalAccount getExternalAccount() {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.READ);
        return new ExternalAccount(getDaoActor().getExternalAccount());
    }

    public final boolean canSetExternalAccount() {
        return new ActorRight.ExternalAccount().canAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
    }

    public final void setExternalAccount(final ExternalAccount externalAccount) {
        new ActorRight.ExternalAccount().tryAccess(calculateRole(getUnprotectedLogin()), Action.WRITE);
        getDaoActor().setExternalAccount(externalAccount.getDao());
    }

    protected DaoActor getDao() {
        return getDaoActor();
    }
}
