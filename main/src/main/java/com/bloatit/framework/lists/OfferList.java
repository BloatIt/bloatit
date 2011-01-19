package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoOffer;

public final class OfferList extends ListBinder<Offer, DaoOffer> {

    public OfferList(final PageIterable<DaoOffer> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Offer> createFromDaoIterator(final Iterator<DaoOffer> dao) {
        return new OfferIterator(dao);
    }

    static final class OfferIterator extends com.bloatit.framework.lists.IteratorBinder<Offer, DaoOffer> {

        public OfferIterator(final Iterable<DaoOffer> daoIterator) {
            super(daoIterator);
        }

        public OfferIterator(final Iterator<DaoOffer> daoIterator) {
            super(daoIterator);
        }

        @Override
        protected Offer createFromDao(final DaoOffer dao) {
            return Offer.create(dao);
        }

    }

}
