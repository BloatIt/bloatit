package com.bloatit.model;

import java.util.Date;

import com.bloatit.model.data.DaoActor;

public abstract class Actor {

    protected abstract DaoActor getDaoActor();
    
    public String getEmail() {
        return getDaoActor().getEmail();
	}

	public void setEmail(String email) {
	    getDaoActor().setEmail(email);
	}

	public String getLogin() {
	    return getDaoActor().getLogin();
	}

	public Date getDateCreation() {
	    return getDaoActor().getDateCreation();
	}

	public InternalAccount getInternalAccount() {
	    return new InternalAccount(getDaoActor().getInternalAccount());
	}

	public ExternalAccount getExternalAccount() {
	    return new ExternalAccount(getDaoActor().getExternalAccount());
	}

	public void setExternalAccount(ExternalAccount externalAccount) {
	    getDaoActor().setExternalAccount(externalAccount.getDao());
	}

	public Integer getId() {
	    return getDaoActor().getId();
	}
}
