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
package com.bloatit.model.managers;

import com.bloatit.data.DaoOffer;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Offer;
import com.bloatit.model.lists.OfferList;

/**
 * The Class OfferManager is an utility class containing static methods.
 */
public final class OfferManager {

    /**
     * Desactivated constructor on utility class.
     */
    private OfferManager() {
        // Desactivate default constructor (sometimes named ctor)
    }

    /**
     * Gets the offer by id.
     * 
     * @param id the id
     * @return the offer or <code>null</code> if not found.
     */
    public static Offer getById(final Integer id) {
        return Offer.create(DBRequests.getById(DaoOffer.class, id));
    }

    public static PageIterable<Offer> getAll() {
        return new OfferList(DBRequests.getAll(DaoOffer.class));
    }

}
