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
        dao.setAccepted();
    }

    /**
     * @see DaoBankTransaction#setRefused()
     */
    public void setRefused() {
        dao.setRefused();
    }

    /**
     * @see DaoBankTransaction#validated()
     */
    public boolean validated() {
        return dao.validated();
    }

    public String getMessage() {
        return dao.getMessage();
    }

    public BigDecimal getValue() {
        return dao.getValue();
    }

    public State getState() {
        return dao.getState();
    }

    public Date getCreationDate() {
        return dao.getCreationDate();
    }

    public Date getModificationDate() {
        return dao.getModificationDate();
    }

    public String getReference() {
        return dao.getReference();
    }

    public String getToken() {
        return dao.getToken();
    }

    public void setProcessInformations(final String processInformations) {
        dao.setProcessInformations(processInformations);
    }

    public String getProcessInformations() {
        return dao.getProcessInformations();
    }
}
