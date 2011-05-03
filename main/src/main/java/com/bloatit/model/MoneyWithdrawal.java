package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.mails.ElveosMail.WithdrawalComplete;

/**
 * Money withdrawals represent requests to withdraw money from the user internal
 * account back to his bank account.
 */
public class MoneyWithdrawal extends Identifiable<DaoMoneyWithdrawal> {
    private static final BigDecimal ibanCheckingConstant = new BigDecimal(97);

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
    public MoneyWithdrawal(Actor<? extends DaoActor> actor, final String IBAN, final String reference, final BigDecimal amountWithdrawn) {
        super(DaoMoneyWithdrawal.createAndPersist(actor.getDao(), IBAN, reference, amountWithdrawn));
        if (!checkIban(IBAN)) {
            throw new BadProgrammerException("Invalid IBAN format!");
        }
    }

    private MoneyWithdrawal(DaoMoneyWithdrawal dao) {
        super(dao);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Setters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Indicates the user wants to cancel the withdrawal.
     * <p>
     * Can only be used while in state REQUESTED
     * </p>
     */
    public void setCanceled() {
        switch (getState()) {
            case COMPLETE:
            case CANCELED:
            case REFUSED:
            case TREATED:
                throw new BadProgrammerException("Cannot go from state " + getState() + " to CANCELED.");
            default:
                break;
        }
        getDao().setCanceled();
    }

    /**
     * Indicates the admins want to refused the withdrawal.
     * <p>
     * Can only be used while in state REQUESTED or TREATED
     * </p>
     */
    public void setRefused() {
        switch (getState()) {
            case COMPLETE:
            case CANCELED:
            case REFUSED:
                throw new BadProgrammerException("Cannot go from state " + getState() + " to REFUSED.");
            default:
                break;
        }
        getDao().setRefused();
    }

    /**
     * Indicates the admins asked their banks to pass the withdrawal.
     * <p>
     * Can only be used while in state REQUESTED
     * </p>
     */
    public void setTreated() {
        switch (getState()) {
            case COMPLETE:
            case CANCELED:
            case REFUSED:
            case TREATED:
                throw new BadProgrammerException("Cannot go from state " + getState() + " to TREATED.");
            default:
                break;
        }
        getDao().setTreated();
    }

    /**
     * Indicates the bank dealth with the money transfer.
     * <p>
     * Can only be used while in state TREATED
     * </p>
     */
    public void setComplete() {
        switch (getState()) {
            case COMPLETE:
            case CANCELED:
            case REFUSED:
            case REQUESTED:
                throw new BadProgrammerException("Cannot go from state " + getState() + " to COMPLETE.");
            default:
                break;
        }
        getDao().setComplete();

        if (getActor() instanceof Member) {
            Member to = (Member) getActor();
            new WithdrawalComplete(getReference(), getAmountWithdrawn().toPlainString(), getIBAN()).sendMail(to, "withdrawal-complete");
        } else {
            // TODO send a mail to some people in team ...
        }
    }

    /**
     * Proxy to the various methods to change the state
     *
     * @param newState the new state of the withdrawal
     * @throws BadProgrammerException whenever <code>newState</code> is not
     *             compatible with current withdrawal state.
     */
    public void setState(State newState) {
        switch (newState) {
            case REQUESTED:
                throw new BadProgrammerException("Cannot go back to Requested");
            case TREATED:
                setTreated();
                break;
            case COMPLETE:
                setComplete();
                break;
            case CANCELED:
                setCanceled();
                break;
            case REFUSED:
                setRefused();
                break;
        }
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    public Actor<?> getActor() {
        Integer id = getDao().getActor().getId();
        try {
            Team team;
            team = Team.class.cast(GenericConstructor.create(Team.class, id));
            if (team != null) {
                return team;
            }
        } catch (ClassNotFoundException e) {
        }

        try {
            Member member;
            member = Member.class.cast(GenericConstructor.create(Member.class, id));
            if (member != null) {
                return member;
            }
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

    public Transaction getTransaction() {
        return Transaction.create(getDao().getTransaction());
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

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Static
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Checks if a String matches the IBAN formatting
     */
    public static boolean checkIban(String iban) {
        iban = iban.replace(" ", "");
        iban = iban.replace("-", "");
        StringBuffer sbIban = new StringBuffer(iban.substring(4));
        sbIban.append(iban.substring(0, 4));
        iban = sbIban.toString();

        StringBuilder extendedIban = new StringBuilder(iban.length());
        for (char currentChar : iban.toCharArray()) {
            extendedIban.append(Character.digit(currentChar, Character.MAX_RADIX));
        }

        try {
            return new BigDecimal(extendedIban.toString()).remainder(ibanCheckingConstant).intValue() == 1;
        } catch (NumberFormatException e) {
            return false;
        }

    }
}
