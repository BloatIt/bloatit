package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.model.Offer;
import com.bloatit.model.data.DaoOffer;

public class OfferIterator extends com.bloatit.framework.lists.IteratorBinder<Offer, DaoOffer> {

    public OfferIterator(Iterable<DaoOffer> daoIterator) {
        super(daoIterator);
    }

    public OfferIterator(Iterator<DaoOffer> daoIterator) {
        super(daoIterator);
    }

    @Override
    protected Offer createFromDao(DaoOffer dao) {
        return new Offer(dao);
    }

}
