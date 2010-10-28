package com.bloatit.framework.lists;

import java.util.Iterator;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoOffer;

public class OfferList extends ListBinder<Offer, DaoOffer> {

    public OfferList(PageIterable<DaoOffer> daoCollection) {
        super(daoCollection);
    }

    @Override
    protected Iterator<Offer> createFromDaoIterator(Iterator<DaoOffer> dao) {
        return new OfferIterator(dao);
    }

}
