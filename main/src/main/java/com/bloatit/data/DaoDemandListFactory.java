package com.bloatit.data;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoDemand.DemandState;

class DaoDemandListFactory extends DaoKudosableListFactory<DaoDemand> {

    private static final String CONTRIBUTION = "contribution";
    private static final String OFFERS = "offers";
    private static final String SELECTED_OFFER = "selectedOffer";
    private static final String PROJECT = "project";
    private static final String DEMAND_STATE = "demandState";

    protected DaoDemandListFactory(Criteria criteria) {
        super(criteria);
    }

    public DaoDemandListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoDemand.class));
    }

    public void stateEquals(DemandState state) {
        add(Restrictions.eq(DEMAND_STATE, state));
    }

    public void projectEquals(DaoProject project) {
        add(Restrictions.eq(PROJECT, project));
    }

    public void selectedOfferIsNotNull() {
        add(Restrictions.isNotNull(SELECTED_OFFER));
    }

    public void selectedOfferIsNull() {
        add(Restrictions.isNull(SELECTED_OFFER));
    }

    public void hasOffers() {
        add(Restrictions.isNotEmpty(OFFERS));
    }

    public void hasNoOffer() {
        add(Restrictions.isEmpty(OFFERS));
    }

    public void hasContributions() {
        add(Restrictions.isNotEmpty(CONTRIBUTION));
    }

    public void hasNoContribution() {
        add(Restrictions.isEmpty(CONTRIBUTION));
    }

    public void contribution(Comparator cmp, BigDecimal value) {
        add(createNbCriterion(cmp, CONTRIBUTION, value));
    }

}
