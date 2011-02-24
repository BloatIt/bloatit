package com.bloatit.model.admin;

import com.bloatit.data.DaoIdentifiable;
import com.bloatit.data.IdentifiableInterface;
import com.bloatit.data.queries.DaoIdentifiableListFactory;
import com.bloatit.framework.utils.PageIterable;

public abstract class IdentifiableAdminListFactory<T extends DaoIdentifiable, U extends IdentifiableInterface> {

    private final DaoIdentifiableListFactory<T> factory;

    protected IdentifiableAdminListFactory(DaoIdentifiableListFactory<T> factory) {
        super();
        this.factory = factory;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public PageIterable<U> list() {
        return (PageIterable) new AdminList<T, U>((PageIterable<T>) factory.createCollection());
    }

    protected DaoIdentifiableListFactory<T> getfactory() {
        return factory;
    }

}
