package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoOffer;

public class OfferIterator extends com.bloatit.framework.lists.IteratorBinder<Offer, DaoOffer> {

    public OfferIterator(final Iterable<DaoOffer> daoIterator) {
        super(daoIterator);
    }

    public OfferIterator(final Iterator<DaoOffer> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Offer createFromDao(final DaoOffer dao) {
        return new Offer(dao);
    }

}
