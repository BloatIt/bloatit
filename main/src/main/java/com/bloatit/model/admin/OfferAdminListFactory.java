package com.bloatit.model.admin;

import java.math.BigDecimal;

import com.bloatit.data.DaoOffer;
import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.data.queries.DaoOfferQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Offer;
import com.bloatit.model.lists.OfferList;

public class OfferAdminListFactory extends KudosableAdminListFactory<DaoOffer, Offer> {


    public OfferAdminListFactory() {
        super(new DaoOfferQuery());
    }

    @Override
    protected DaoOfferQuery getfactory() {
        return (DaoOfferQuery) super.getfactory();
    }

    @Override
    public PageIterable<Offer> list() {
        return new OfferList(getfactory().createCollection());
    }

    public void amount(Comparator cmp, BigDecimal value) {
        getfactory().amount(cmp, value);
    }

    public void withBatches() {
        getfactory().withBatches();
    }

    public void withoutBatches() {
        getfactory().withoutBatches();
    }

}
