package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoBankTransaction.State;

/**
 * @see DaoBankTransaction
 */
@Entity
public final class BankTransaction extends Identifiable<DaoBankTransaction> {

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

    public BankTransaction(final String message, final String token, final DaoActor author, final BigDecimal value, final String orderReference) {
        super(DaoBankTransaction.createAndPersist(message, token, author, value, orderReference));
    }

    private BankTransaction(final DaoBankTransaction dao) {
        super(dao);
    }

    /**
     * @see DaoBankTransaction#setAccepted()
     */
    public void setAccepted() {
        getDao().setAccepted();
    }

    /**
     * @see DaoBankTransaction#setRefused()
     */
    public void setRefused() {
        getDao().setRefused();
    }

    /**
     * @see DaoBankTransaction#validated()
     */
    public boolean validated() {
        return getDao().validated();
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
}
