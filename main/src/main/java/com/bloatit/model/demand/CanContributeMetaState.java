//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
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
