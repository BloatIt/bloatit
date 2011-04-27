//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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
