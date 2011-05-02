package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.data.DaoTransaction;

public class MoneyWithdrawal extends Identifiable<DaoMoneyWithdrawal> {

    /**
     * The Class MyCreator.
     */
    private static final class MyCreator extends Creator<DaoMoneyWithdrawal, MoneyWithdrawal> {
        @SuppressWarnings("synthetic-access")
        @Override
        public MoneyWithdrawal doCreate(final DaoMoneyWithdrawal dao) {
            return new MoneyWithdrawal(dao);
        }
    }

    /**
     * Check the cache, if a corresponding MoneyWithdrawal exist return it,
     * otherwise create a MoneyWithdrawal using its dao representation. If the
     * dao == null return null;
     * 
     * @param dao the dao
     * @return the bank transaction
     */
    @SuppressWarnings("synthetic-access")
    public static MoneyWithdrawal create(final DaoMoneyWithdrawal dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Creates a new Money withdrawal request
     * 
     * @param IBAN
     * @param reference
     * @param amountWithdrawn
     */
    public MoneyWithdrawal(Actor<DaoActor> actor, final String IBAN, final String reference, final BigDecimal amountWithdrawn) {
        super(DaoMoneyWithdrawal.createAndPersist(actor.getDao(), IBAN, reference, amountWithdrawn));
    }

    private MoneyWithdrawal(DaoMoneyWithdrawal dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public DaoActor getActor() {
        return getDao().getActor();
    }

    public DaoTransaction getTransaction() {
        return getDao().getTransaction();
    }

    public String getIBAN() {
        return getDao().getIBAN();
    }

    public BigDecimal getAmountWithdrawn() {
        return getDao().getAmountWithdrawn();
    }

    public Date getCreationDate() {
        return getDao().getCreationDate();
    }

    public Date getLastModificationDate() {
        return getDao().getLastModificationDate();
    }

    public String getReference() {
        return getDao().getReference();
    }

    public String getComment() {
        return getDao().getComment();
    }

    public State getState() {
        return getDao().getState();
    }

    public String getRefusalReason() {
        return getDao().getRefusalReason();
    }

    @Override
    protected boolean isMine(Member member) {
        return getDao().getActor().equals(member);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }
}
