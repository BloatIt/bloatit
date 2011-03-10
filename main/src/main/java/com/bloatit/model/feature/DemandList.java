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
package com.bloatit.model.feature;

import java.util.Iterator;

import com.bloatit.data.DaoFeature;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Feature;
import com.bloatit.model.lists.ListBinder;

/**
 * The Class DemandList. It is a ListBinder to transform PageIterable<DaoDemand>
 * to PageIterable<Demand>
 */
public final class DemandList implements PageIterable<Feature> {
    private final ListBinder<DemandImplementation, DaoFeature> listBinder;

    public DemandList(PageIterable<DaoFeature> daoList) {
        listBinder = new ListBinder<DemandImplementation, DaoFeature>(daoList);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final Iterator<Feature> iterator() {
        return (Iterator) listBinder.iterator();
    }

    @Override
    public final void setPage(int page) {
        listBinder.setPage(page);
    }

    @Override
    public final void setPageSize(int pageSize) {
        listBinder.setPageSize(pageSize);
    }

    @Override
    public final int getPageSize() {
        return listBinder.getPageSize();
    }

    @Override
    public final int size() {
        return listBinder.size();
    }

    @Override
    public final int pageNumber() {
        return listBinder.pageNumber();
    }

    @Override
    public final int getCurrentPage() {
        return listBinder.getCurrentPage();
    }

}
