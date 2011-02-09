package com.bloatit.model.demand;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoOffer;

public abstract class CanContributeMetaState extends AbstractDemandState {

    public CanContributeMetaState(final DemandImplementation demand) {
        super(demand);
    }

    protected abstract AbstractDemandState notifyAddContribution();

    @Override
    public final AbstractDemandState eventAddContribution() {
        return notifyAddContribution();
    }

    protected final AbstractDemandState handleEvent() {
        final BigDecimal contribution = demand.getDao().getContribution();
        final DaoOffer selectedOffer = demand.getDao().getSelectedOffer();
        final Date validationDate = demand.getDao().getValidationDate();
        if (selectedOffer != null && validationDate != null && contribution.compareTo(selectedOffer.getAmount()) >= 0
                && new Date().after(validationDate)) {
            return new DeveloppingState(demand);
        }
        return this;
    }

}
