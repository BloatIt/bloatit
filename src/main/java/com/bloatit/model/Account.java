package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.common.FatalErrorException;
import com.bloatit.common.PageIterable;
import com.bloatit.framework.lists.TransactionList;
import com.bloatit.model.data.DaoAccount;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoMember;

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

	public Actor getActor() {
	    if (getDaoAccount().getActor().getClass() == DaoMember.class){
	        return new Member((DaoMember) getDaoAccount().getActor());
	    }else if(getDaoAccount().getActor().getClass() == DaoGroup.class){
            return new Group((DaoGroup) getDaoAccount().getActor());
        }
	    throw new FatalErrorException("Cannot find the right Actor child class.", null);
	}

	public Date getCreationDate() {
	    return getDaoAccount().getCreationDate();
	}

    @Override
    protected int getDaoId() {
        return getDaoAccount().getId();
    }
	
	
}