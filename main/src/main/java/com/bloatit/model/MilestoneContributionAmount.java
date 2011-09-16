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
package com.bloatit.model;

import java.math.BigDecimal;

import com.bloatit.data.DaoMilestoneContributionAmount;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtContribution;
import com.bloatit.model.right.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.model.visitor.ModelClassVisitor;

/**
 * This is a invoice.
 */
public final class MilestoneContributionAmount extends Identifiable<DaoMilestoneContributionAmount> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This class implements the method pattern, implementing the doCreate
     * method. See the base class for more informations: {@link Creator}.
     */
    private static final class MyCreator extends Creator<DaoMilestoneContributionAmount, MilestoneContributionAmount> {

        /*
         * (non-Javadoc)
         * @see
         * com.bloatit.model.Creator#doCreate(com.bloatit.data.DaoIdentifiable)
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public MilestoneContributionAmount doCreate(final DaoMilestoneContributionAmount dao) {
            return new MilestoneContributionAmount(dao);
        }
    }

    /**
     * Find a bug in the cache or create an new one.
     * 
     * @param dao the dao
     * @return null if dao is null. Else return the new invoice.
     */
    @SuppressWarnings("synthetic-access")
    public static MilestoneContributionAmount create(final DaoMilestoneContributionAmount dao) {
        return new MyCreator().create(dao);
    }

    /**
     * Instantiates a new invoice.
     * 
     * @param dao the dao
     */
    private MilestoneContributionAmount(final DaoMilestoneContributionAmount dao) {
        super(dao);
    }

    public Contribution getContribution() {
        return Contribution.create(getDao().getContribution());
    }

    public Milestone getMilestone() {
        return Milestone.create(getDao().getMilestone());
    }

    public BigDecimal getAmount() throws UnauthorizedPublicReadOnlyAccessException {
        tryAccess(new RgtContribution.Amount(), Action.READ);
        return getDao().getAmount();
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
