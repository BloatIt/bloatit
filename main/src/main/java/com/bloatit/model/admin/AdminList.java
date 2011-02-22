package com.bloatit.model.admin;

import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class TransactionList transforms PageIterable<DaoUserContent> to
 * PageIterable<Transaction>.
 */
public class AdminList<T extends DaoUserContent, U extends UserContentAdmin<T>> extends ListBinder<U, T> {

    public AdminList(PageIterable<T> daoCollection) {
        super(daoCollection);
    }
}
