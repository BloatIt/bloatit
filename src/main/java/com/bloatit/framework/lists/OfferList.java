package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoOffer;

public class OfferList extends ListBinder<Offer, DaoOffer> {

    public OfferList(final PageIterable<DaoOffer> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Offer> createFromDaoIterator(final Iterator<DaoOffer> dao) {
        return new OfferIterator(dao);
    }

}
