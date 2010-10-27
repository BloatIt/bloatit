package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TransactionList;
import com.bloatit.model.data.DaoAccount;

public abstract class Account extends Identifiable{
    
    protected abstract DaoAccount getDaoAccount();
    
	public Date getLastModificationDate() {
	    return getDaoAccount().getLastModificationDate();
	}

	public BigDecimal getAmount() {
	    return getDaoAccount().getAmount();
	}

	public PageIterable<Transaction> getTransactions() {
	    return new TransactionList(getDaoAccount().getTransactions());
	}

	public Integer getId() {
	    return getDaoAccount().getId();
	}

	public Actor getActor() {
	    // TODO find if it is a group or a member. (Factory ?)
	    // TODO implement me 
	    return null; //new Actor(daoAccount.getActor());
	}

	public Date getCreationDate() {
	    return getDaoAccount().getCreationDate();
	}

    @Override
    protected int getDaoId() {
        return getDaoAccount().getId();
    }
	
	
}