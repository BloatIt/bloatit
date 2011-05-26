package com.bloatit.model.right;

/**
 * Rights to handle money withdrawal informations.
 * <p>
 * Most information are BankData (can only be seen by owner or people with bank
 * right in the team).
 * </p>
 */
public class RgtMoneyWithdrawal extends RightManager {

    /**
     * The Class Comment is a {@link RightManager.AdminOnly} accessor for the Contact property.
     */
    public static class Comment extends AdminOnly {
        // rename only
    }
    
    /**
     * The Class State is a {@link RightManager.ReadOnlyBankData} accessor for the Contact property.
     */
    public static class State extends ReadOnlyBankData {
        // rename only
    }
    
    /**
     * The Class Actor is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Actor extends BankData {
        // rename only
    }

    /**
     * The Class Canceled is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Canceled extends BankData {
        // rename only
    }
    
    /**
     * The Class Transaction is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Transaction extends BankData {
        // rename only
    }
    
    /**
     * The Class Iban is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Iban extends BankData {
        // rename only
    }
    
    
    /**
     * The Class Amount is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Amount extends BankData {
        // rename only
    }
    
    
    /**
     * The Class CreationDate is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class CreationDate extends BankData {
        // rename only
    }
    
    
    /**
     * The Class LastModificationDate is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class LastModificationDate extends BankData {
        // rename only
    }
    
    /**
     * The Class Reference is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class Reference extends BankData {
        // rename only
    }
    
    /**
     * The Class RefusalReason is a {@link RightManager.BankData} accessor for the Contact property.
     */
    public static class RefusalReason extends BankData {
        // rename only
    }
}
