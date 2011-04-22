package com.bloatit.model.admin;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.data.queries.DaoIdentifiableQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.IdentifiableInterface;

public abstract class IdentifiableAdminListFactory<T extends DaoIdentifiable, U extends IdentifiableInterface> {

    private final DaoIdentifiableQuery<T> factory;

    protected IdentifiableAdminListFactory(final DaoIdentifiableQuery<T> factory) {
        super();
        this.factory = factory;
    }

    public PageIterable<U> list() {
        return new AdminList<T, U>(factory.createCollection());
    }

    protected DaoIdentifiableQuery<T> getfactory() {
        return factory;
    }

    public void orderBy(final String column, final OrderType orderType) {
        getfactory().orderBy(column, orderType);
    }
}
