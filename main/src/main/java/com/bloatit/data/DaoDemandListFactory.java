package com.bloatit.data;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.DaoKudosable.PopularityState;

class DaoDemandListFactory extends DaoKudosableListFactory<DaoDemand> {

    protected DaoDemandListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoDemandListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoDemand.class));
    }

    public void orderByNbComments(DaoAbstractListFactory.OrderType order) {
        // TODO
    }

    public void orderByNbContributions(DaoAbstractListFactory.OrderType order) {
        // TODO
    }

    public void orderByNbOffers(DaoAbstractListFactory.OrderType order) {
        // TODO
    }

    public void stateEquals(DemandState state) {
        add(Restrictions.eq("demandState", state));
    }

    public void projectEquals(DaoProject project) {
        add(Restrictions.eq("project", project));
    }

    public void selectedOfferIsNotNull() {
        add(Restrictions.isNotNull("selectedOffer"));
    }

    public void selectedOfferIsNull() {
        add(Restrictions.isNull("selectedOffer"));
    }

    public void hasOffers() {
        add(Restrictions.isNotEmpty("offers"));
    }

    public void hasNoOffer() {
        add(Restrictions.isEmpty("offers"));
    }

    public void hasContributions() {
        add(Restrictions.isNotEmpty("contribution"));
    }

    public void hasNoContribution() {
        add(Restrictions.isEmpty("contribution"));
    }

    public void contribution(Comparator cmp, BigDecimal value) {
        add(createNbCriterion(cmp, "contribution", value));
    }

}
