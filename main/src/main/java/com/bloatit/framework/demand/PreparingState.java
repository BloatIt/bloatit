package com.bloatit.framework.demand;

import java.math.BigDecimal;

import com.bloatit.framework.Offer;
import com.bloatit.model.data.DaoDemand.DemandState;

public class PreparingState extends AbstractDemandState {
    public PreparingState(Demand demand) {
        super(demand);
        demand.getDao().setDemandState(DemandState.PREPARING);
    }

    @Override
    public AbstractDemandState addOffer(Offer offer) {
        demand.setSelectedOffer(offer);
        return this;
    }

    @Override
    public AbstractDemandState removeOffer(Offer offer) {
        if (demand.getDao().getOffers().size() > 0) {
            return this;
        }
        return new PendingState(demand);
    }

    @Override
    public AbstractDemandState selectedOfferTimeOut(BigDecimal contribution, Offer offer) {
        return handleEvent(contribution, offer);
    }

    @Override
    public AbstractDemandState addContribution(BigDecimal contribution, Offer offer) {
        return handleEvent(contribution, offer);
    }

    private AbstractDemandState handleEvent(BigDecimal contribution, Offer offer) {
        if (offer != null && contribution.compareTo(offer.getAmount()) >= 0) {
            return new DeveloppingState(demand);
        }
        return this;
    }
}