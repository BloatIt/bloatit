package com.bloatit.model.lists;

import com.bloatit.data.DaoMoneyWithdrawal;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.MoneyWithdrawal;

public class MoneyWithdrawalList extends ListBinder<MoneyWithdrawal, DaoMoneyWithdrawal> {
    public MoneyWithdrawalList(final PageIterable<DaoMoneyWithdrawal> daoCollection) {
        super(daoCollection);
    }
}
