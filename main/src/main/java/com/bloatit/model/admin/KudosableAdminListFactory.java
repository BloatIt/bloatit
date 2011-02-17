package com.bloatit.model.admin;

import com.bloatit.data.DaoKudosable;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.queries.DaoAbstractListFactory.Comparator;
import com.bloatit.data.queries.DaoAbstractListFactory.OrderType;
import com.bloatit.data.queries.DaoKudosableListFactory;
import com.bloatit.framework.utils.PageIterable;

public class KudosableAdminListFactory<T extends DaoKudosable, U extends KudosableAdmin<T>> extends UserContentAdminListFactory<T, U> {

    public KudosableAdminListFactory() {
        super(new DaoKudosableListFactory<T>());
    }

    protected KudosableAdminListFactory(DaoKudosableListFactory<T> factory) {
        super(factory);
    }

    @Override
    protected DaoKudosableListFactory<T> getfactory() {
        return (DaoKudosableListFactory<T>) super.getfactory();
    }

    public void orderByPopularity(OrderType order) {
        getfactory().orderByPopularity(order);
    }

    public void popularity(Comparator cmp, int value) {
        getfactory().popularity(cmp, value);
    }

    public void stateEquals(PopularityState state) {
        getfactory().stateEquals(state);
    }

    public void kudosSize(Comparator cmp, int number) {
        getfactory().kudosSize(cmp, number);
    }

    public void lokedOnly() {
        getfactory().lokedOnly();
    }

    public void nonLokedOnly() {
        getfactory().nonLokedOnly();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PageIterable<U> list() {
        return (PageIterable<U>) new AdminList.KudosableAdminList((PageIterable<DaoKudosable>) getfactory().createCollection());
    }

}