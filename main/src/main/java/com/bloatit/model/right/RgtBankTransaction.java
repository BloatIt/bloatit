package com.bloatit.model.right;

import com.bloatit.model.BankTransaction;

/**
 * The Class ContributionRight store the properties accessor for the
 * {@link BankTransaction} class.
 */
public class RgtBankTransaction extends RightManager {

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction Message.
     */
    public static class Message extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction ValuePaid.
     */
    public static class ValuePaid extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction Value.
     */
    public static class Value extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction State.
     */
    public static class State extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction CreationDate.
     */
    public static class CreationDate extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction ModificationDate.
     */
    public static class ModificationDate extends ReadOnlyBankData {
        // nothing this is just a rename.
    }
    
    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction Reference.
     */
    public static class Reference extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

    /**
     * The Class RgtBankTransaction is a {@link RightManager.PublicReadOnly} accessor for
     * the Transaction Author.
     */
    public static class Author extends ReadOnlyBankData {
        // nothing this is just a rename.
    }

}
