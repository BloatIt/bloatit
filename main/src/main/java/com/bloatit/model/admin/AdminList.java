package com.bloatit.model.admin;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.IdentifiableInterface;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class TransactionList transforms PageIterable<DaoUserContent> to
 * PageIterable<Transaction>.
 */
public class AdminList<T extends DaoIdentifiable, U extends IdentifiableInterface> extends ListBinder<U, T> {

    protected AdminList(final PageIterable<T> daoCollection) {
        super(daoCollection);
    }
}
