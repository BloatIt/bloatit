package com.bloatit.model.managers;

import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.lists.MoneyWithdrawalList;

public class MoneyWithdrawalManager {

    private MoneyWithdrawalManager() {
        // Disable CTOR
    }

    /**
     * Gets the money withdrawal matching a given id
     */
    public static MoneyWithdrawal getById(final Integer id) {
        return MoneyWithdrawal.create(DBRequests.getById(DaoMoneyWithdrawal.class, id));
    }

    /**
     * @return all the money withdrawals
     */
    public static MoneyWithdrawalList getAll() {
        return new MoneyWithdrawalList(DBRequests.getAll(DaoMoneyWithdrawal.class));
    }

    /**
     * Finds all the money withdrawal that are in a given state
     * 
     * @param state the state of the money withdrawals to find
     * @return a list of money withdrawals that match <code>state</code>
     */
    public static MoneyWithdrawalList getByState(final State state) {
        return new MoneyWithdrawalList(DaoMoneyWithdrawal.getByState(state));
    }
}
