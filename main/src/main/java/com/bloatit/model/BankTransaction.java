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
public final class BankTransaction extends Identifiable {

    private final DaoBankTransaction dao;

    public static BankTransaction create(final DaoBankTransaction daoBankTransaction) {
        if (daoBankTransaction != null) {
            return new BankTransaction(daoBankTransaction);
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
        this.dao = DaoBankTransaction.createAndPersist(message, token, author, value, orderReference);
    }

    private BankTransaction(final DaoBankTransaction dao) {
        super();
        this.dao = dao;
    }

    /**
     * @see Identifiable#getId()
     */
    @Override
    public int getId() {
        return dao.getId();
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
