package com.bloatit.model.demand;

import java.math.BigDecimal;
import java.util.Date;

import com.bloatit.data.DaoOffer;

public abstract class CanContributeMetaState extends AbstractDemandState {

    public CanContributeMetaState(final DemandImplementation demandImplementation) {
        super(demandImplementation);
    }

    protected abstract AbstractDemandState notifyAddContribution();

    @Override
    public final AbstractDemandState eventAddContribution() {
        return notifyAddContribution();
    }

    protected final AbstractDemandState handleEvent() {
        final BigDecimal contribution = demandImplementation.getDao().getContribution();
        final DaoOffer selectedOffer = demandImplementation.getDao().getSelectedOffer();
        final Date validationDate = demandImplementation.getDao().getValidationDate();
        if (selectedOffer != null && validationDate != null && contribution.compareTo(selectedOffer.getAmount()) >= 0
                && new Date().after(validationDate)) {
            return new DeveloppingState(demandImplementation);
        }
        return this;
    }

}
