package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;

import com.bloatit.data.DaoActor;
import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.mails.ElveosMail.WithdrawalAdminMail;
import com.bloatit.framework.mails.ElveosMail.WithdrawalCompleteMail;
import com.bloatit.framework.mails.ElveosMail.WithdrawalRequestedMail;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtMoneyWithdrawal;
import com.bloatit.model.right.UnauthorizedBankDataAccessException;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.UnauthorizedReadOnlyBankDataAccessException;
import com.bloatit.web.url.MoneyWithdrawalAdminPageUrl;

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
    public MoneyWithdrawal(final Actor<? extends DaoActor> actor, final String IBAN, final BigDecimal amountWithdrawn) {
        super(DaoMoneyWithdrawal.createAndPersist(actor.getDao(), IBAN, generateReference(), amountWithdrawn));
        if (!checkIban(IBAN)) {
            throw new BadProgrammerException("Invalid IBAN format!");
        }
        final String reference = getReferenceUnprotected();
        final String amount = getAmountWithdrawnUnprotected().toPlainString();
        final String iban = getIBANUnprotected();
        if (getActorUnprotected() instanceof Member) {
            final Member to = (Member) getActorUnprotected();
            new WithdrawalRequestedMail(reference, amount, iban).sendMail(to, "withdrawal-request");
        } else {
            // TODO send a mail to some people in team ...
        }

        // Sending email to administrators
        for (final String email : ModelConfiguration.getAdminstratorMails()) {
            new WithdrawalAdminMail(reference,
                                    amount,
                                    iban,
                                    getActorUnprotected().getDisplayName(),
                                    new MoneyWithdrawalAdminPageUrl().externalUrlString()).sendMail(email, "withdrawal-admin");
        }
    }

    private MoneyWithdrawal(final DaoMoneyWithdrawal dao) {
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
     * 
     * @throws UnauthorizedOperationException
     */
    public void setCanceled() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.Canceled(), Action.WRITE);
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
     * 
     * @throws UnauthorizedOperationException
     */
    public void setRefused() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.State(), Action.WRITE);
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
     * 
     * @throws UnauthorizedOperationException
     */
    public void setTreated() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.State(), Action.WRITE);
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
     * Indicates the bank dealt with the money transfer.
     * <p>
     * Can only be used while in state TREATED
     * </p>
     * 
     * @throws UnauthorizedOperationException
     */
    public void setComplete() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.State(), Action.WRITE);
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
            final Member to = (Member) getActor();
            new WithdrawalCompleteMail(getReference(), getAmountWithdrawn().toPlainString(), getIBAN()).sendMail(to, "withdrawal-complete");
        } else {
            // TODO send a mail to some people in team ...
        }
    }

    /**
     * Proxy to the various methods to change the state
     * 
     * @param newState the new state of the withdrawal
     * @throws UnauthorizedPrivateAccessException
     * @throws BadProgrammerException whenever <code>newState</code> is not
     *             compatible with current withdrawal state.
     */
    public void setState(final State newState) throws UnauthorizedOperationException {
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

    public boolean canSetCanceled() {
        if (!canAccess(new RgtMoneyWithdrawal.Canceled(), Action.WRITE)) {
            return false;
        }
        try {
            switch (getState()) {
                case COMPLETE:
                case CANCELED:
                case REFUSED:
                case TREATED:
                    return false;
                default:
                    break;
            }
        } catch (final UnauthorizedOperationException e) {
            return false;
        }
        return true;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Getters
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the actor of the request (either a member or a team)
     * @throws UnauthorizedBankDataAccessException
     */
    public Actor<?> getActor() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.Actor(), Action.READ);
        return getActorUnprotected();
    }

    protected Actor<?> getActorUnprotected() {
        return Actor.getActorFromDao(getDao().getActor());
    }

    /**
     * @return the {@link MoneyWithdrawal} transaction, or <i>null</i> if there
     *         is no transaction yet
     * @throws UnauthorizedBankDataAccessException
     */
    public Transaction getTransaction() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.Transaction(), Action.READ);
        return Transaction.create(getDao().getTransaction());
    }

    /**
     * @return the money transaction IBAN
     * @throws UnauthorizedBankDataAccessException
     */
    public String getIBAN() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.Iban(), Action.READ);
        return getIBANUnprotected();
    }

    private String getIBANUnprotected() {
        return getDao().getIBAN();
    }

    /**
     * @return the amount of the money withdrawal
     * @throws UnauthorizedBankDataAccessException
     */
    public BigDecimal getAmountWithdrawn() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.Amount(), Action.READ);
        return getAmountWithdrawnUnprotected();
    }

    private BigDecimal getAmountWithdrawnUnprotected() {
        return getDao().getAmountWithdrawn();
    }

    /**
     * @return the creation date of the money withdrawal
     * @throws UnauthorizedBankDataAccessException
     */
    public Date getCreationDate() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.CreationDate(), Action.READ);
        return getDao().getCreationDate();
    }

    /**
     * Last modification date is the last time the state of the money withdrawal
     * has been changed. If no change happened, this will be the same as
     * creation date.
     * 
     * @return the last modification date of the money withdrawal
     * @throws UnauthorizedBankDataAccessException
     */
    public Date getLastModificationDate() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.LastModificationDate(), Action.READ);
        return getDao().getLastModificationDate();
    }

    /**
     * @return the unique reference of the Money Withdrawal
     * @throws UnauthorizedBankDataAccessException
     */
    public String getReference() throws UnauthorizedBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.Reference(), Action.READ);
        return getReferenceUnprotected();
    }

    private String getReferenceUnprotected() {
        return getDao().getReference();
    }

    /**
     * @return the comment the admins added to this request
     * @throws UnauthorizedOperationException
     */
    public String getComment() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.Comment(), Action.READ);
        return getDao().getComment();
    }

    /**
     * @return the current state of the withdrawal
     * @throws UnauthorizedReadOnlyBankDataAccessException
     */
    public State getState() throws UnauthorizedReadOnlyBankDataAccessException {
        tryAccess(new RgtMoneyWithdrawal.State(), Action.READ);
        return getDao().getState();
    }

    /**
     * @return the reason why this request has been refused by admins, or null
     *         if it is not refused
     * @throws UnauthorizedOperationException
     */
    public String getRefusalReason() throws UnauthorizedOperationException {
        tryAccess(new RgtMoneyWithdrawal.RefusalReason(), Action.READ);
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
        final StringBuffer sbIban = new StringBuffer(iban.substring(4));
        sbIban.append(iban.substring(0, 4));
        iban = sbIban.toString();

        final StringBuilder extendedIban = new StringBuilder(iban.length());
        for (final char currentChar : iban.toCharArray()) {
            extendedIban.append(Character.digit(currentChar, Character.MAX_RADIX));
        }

        try {
            return new BigDecimal(extendedIban.toString()).remainder(ibanCheckingConstant).intValue() == 1;
        } catch (final NumberFormatException e) {
            return false;
        }

    }

    /**
     * @return a random string to use as reference to the team
     */
    private static String generateReference() {
        return RandomStringUtils.randomAlphanumeric(4) + "-" + RandomStringUtils.randomAlphanumeric(10);
    }
}
