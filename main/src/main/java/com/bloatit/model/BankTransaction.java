//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBankTransaction.State;

/**
 * @see DaoBankTransaction
 */
@Entity
public final class BankTransaction extends Identifiable<DaoBankTransaction> {

    /**
     * Check the cache, if a corresponding BankTransaction exist return it, otherwise
     * create a BankTransaction using its dao representation. If the dao == null return
     * null;
     */
    public static BankTransaction create(final DaoBankTransaction dao) {
        if (dao != null) {
            @SuppressWarnings("unchecked")
            final Identifiable<DaoBankTransaction> created = CacheManager.get(dao);
            if (created == null) {
                return new BankTransaction(dao);
            }
            return (BankTransaction) created;
        }
        return null;
    }

    /**
     * @see DaoBankTransaction#getByToken(String)
     */
    public static BankTransaction getByToken(final String token) {
        return create(DaoBankTransaction.getByToken(token));
    }

    /**
     * Create a new BankTransaction.
     * 
     * @param message is the message from the bank. May be a Ok message or an error
     * message.
     * @param token is a token to authenticate this transaction. The online bank service
     * should give it during the transaction.
     * @param author it the person implied in this transaction (the one filling is
     * account)
     * @param value is the quantity of money transfered.
     * @param orderReference is a reference we have to create and should be unique.
     */
    public BankTransaction(final String message, final String token, final Actor<?> author, final BigDecimal value, final String orderReference) {
        super(DaoBankTransaction.createAndPersist(message, token, author.getDao(), value, orderReference));
    }

    private BankTransaction(final DaoBankTransaction dao) {
        super(dao);
    }

    /**
     * @see DaoBankTransaction#setAuthorized()
     */
    public void setAuthorized() {
        getDao().setAuthorized();
    }

    /**
     * @see DaoBankTransaction#setRefused()
     */
    public void setRefused() {
        getDao().setRefused();
    }

    /**
     * @see DaoBankTransaction#setValidated()
     */
    public boolean setValidated() {
        return getDao().setValidated();
    }

    public String getMessage() {
        return getDao().getMessage();
    }

    public BigDecimal getValue() {
        return getDao().getValue();
    }

    public State getState() {
        return getDao().getState();
    }

    public Date getCreationDate() {
        return getDao().getCreationDate();
    }

    public Date getModificationDate() {
        return getDao().getModificationDate();
    }

    public String getReference() {
        return getDao().getReference();
    }

    public String getToken() {
        return getDao().getToken();
    }

    public void setProcessInformations(final String processInformations) {
        getDao().setProcessInformations(processInformations);
    }

    public String getProcessInformations() {
        return getDao().getProcessInformations();
    }

    @Override
    protected boolean isMine(Member member) {
        return getDao().getAuthor().getLogin().equals(member.getLoginUnprotected());
    }
}
