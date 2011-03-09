package com.bloatit.model.admin;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.data.queries.DaoIdentifiableQuery;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.framework.utils.PageIterable;

public abstract class IdentifiableAdminListFactory<T extends DaoIdentifiable, U extends IdentifiableInterface> {

    private final DaoIdentifiableQuery<T> factory;

    protected IdentifiableAdminListFactory(DaoIdentifiableQuery<T> factory) {
        super();
        this.factory = factory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PageIterable<U> list() {
        return (PageIterable) new AdminList<T, U>((PageIterable<T>) factory.createCollection());
    }

    protected DaoIdentifiableQuery<T> getfactory() {
        return factory;
    }

    public void orderBy(final String column, final OrderType orderType) {
        getfactory().orderBy(column, orderType);
    }
}
