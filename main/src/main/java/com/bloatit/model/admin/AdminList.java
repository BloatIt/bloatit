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
