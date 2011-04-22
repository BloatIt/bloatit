package com.bloatit.model.admin;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.data.queries.DaoAbstractQuery.OrderType;
import com.bloatit.data.queries.DaoKudosableQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Kudosable;
import com.bloatit.model.KudosableInterface;

public class KudosableAdminListFactory<T extends DaoKudosable, U extends KudosableInterface> extends UserContentAdminListFactory<T, U> {

    public KudosableAdminListFactory() {
        super(new DaoKudosableQuery<T>());
    }

    protected KudosableAdminListFactory(final DaoKudosableQuery<T> factory) {
        super(factory);
    }

    @Override
    protected DaoKudosableQuery<T> getfactory() {
        return (DaoKudosableQuery<T>) super.getfactory();
    }

    public void orderByPopularity(final OrderType order) {
        getfactory().orderByPopularity(order);
    }

    public void popularity(final Comparator cmp, final int value) {
        getfactory().popularity(cmp, value);
    }

    public void stateEquals(final PopularityState state) {
        getfactory().stateEquals(state);
    }

    public void kudosSize(final Comparator cmp, final int number) {
        getfactory().kudosSize(cmp, number);
    }

    public void lokedOnly() {
        getfactory().lokedOnly();
    }

    public void nonLokedOnly() {
        getfactory().nonLokedOnly();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public PageIterable<U> list() {
        return (PageIterable) new AdminList<DaoKudosable, Kudosable<DaoKudosable>>((PageIterable<DaoKudosable>) getfactory().createCollection());
    }

}
