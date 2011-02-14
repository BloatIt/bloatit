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
package com.bloatit.model.lists;

import java.util.Iterator;

import com.bloatit.data.DaoOffer;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Offer;

/**
 * The Class OfferList transforms PageIterable<DaoOffer> to PageIterable<Offer>.
 */
public final class OfferList extends ListBinder<Offer, DaoOffer> {

    /**
     * Instantiates a new offer list.
     * 
     * @param daoCollection the dao collection
     */
    public OfferList(final PageIterable<DaoOffer> daoCollection) {
        super(daoCollection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bloatit.model.lists.ListBinder#createFromDaoIterator(java.util.Iterator)
     */
    @Override
    protected Iterator<Offer> createFromDaoIterator(final Iterator<DaoOffer> dao) {
        return new OfferIterator(dao);
    }

    /**
     * The Class OfferIterator.
     */
    static final class OfferIterator extends com.bloatit.model.lists.IteratorBinder<Offer, DaoOffer> {

        /**
         * Instantiates a new offer iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public OfferIterator(final Iterable<DaoOffer> daoIterator) {
            super(daoIterator);
        }

        /**
         * Instantiates a new offer iterator.
         * 
         * @param daoIterator the dao iterator
         */
        public OfferIterator(final Iterator<DaoOffer> daoIterator) {
            super(daoIterator);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.bloatit.model.lists.IteratorBinder#createFromDao(java.lang.Object)
         */
        @Override
        protected Offer createFromDao(final DaoOffer dao) {
            return Offer.create(dao);
        }

    }

}
