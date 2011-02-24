package com.bloatit.model.admin;

import java.math.BigDecimal;

import com.bloatit.data.DaoOffer;
import com.bloatit.data.queries.DaoAbstractListFactory.Comparator;
import com.bloatit.data.queries.DaoOfferListFactory;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Offer;
import com.bloatit.model.lists.OfferList;

public class OfferAdminListFactory extends KudosableAdminListFactory<DaoOffer, Offer> {


    public OfferAdminListFactory() {
        super(new DaoOfferListFactory());
    }

    @Override
    protected DaoOfferListFactory getfactory() {
        return (DaoOfferListFactory) super.getfactory();
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
