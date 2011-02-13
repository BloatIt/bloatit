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
package com.bloatit.data;

import java.math.BigDecimal;

import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoDemand.DemandState;

/**
 * A Factory to create query on the DB and return list of DaoDemand.
 */
public final class DaoDemandListFactory extends DaoKudosableListFactory<DaoDemand> {

    /** The Constant CONTRIBUTION. */
    private static final String CONTRIBUTION = "contribution";

    /** The Constant OFFERS. */
    private static final String OFFERS = "offers";

    /** The Constant SELECTED_OFFER. */
    private static final String SELECTED_OFFER = "selectedOffer";

    /** The Constant PROJECT. */
    private static final String PROJECT = "project";

    /** The Constant DEMAND_STATE. */
    private static final String DEMAND_STATE = "demandState";

    /**
     * Instantiates a new dao demand list factory.
     */
    public DaoDemandListFactory() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoDemand.class));
    }

    /**
     * Add a WHERE close with demandState = <code>state</code>.
     * 
     * @param state the state
     */
    public void stateEquals(DemandState state) {
        add(Restrictions.eq(DEMAND_STATE, state));
    }

    /**
     * Add a WHERE close with project = <code>project</code>.
     * 
     * @param project the project
     */
    public void projectEquals(DaoProject project) {
        add(Restrictions.eq(PROJECT, project));
    }

    /**
     * Add a WHERE close ensuring that there is a selected offer on each returned
     * daoDemand.
     */
    public void selectedOfferIsNotNull() {
        add(Restrictions.isNotNull(SELECTED_OFFER));
    }

    /**
     * Add a WHERE close ensuring that there isn't a selected offer on each returned
     * daoDemand.
     */
    public void selectedOfferIsNull() {
        add(Restrictions.isNull(SELECTED_OFFER));
    }

    /**
     * Add a WHERE close ensuring that the demands have at least one offer.
     */
    public void hasOffers() {
        add(Restrictions.isNotEmpty(OFFERS));
    }

    /**
     * Add a WHERE close ensuring that the demands have no offer.
     */
    public void hasNoOffer() {
        add(Restrictions.isEmpty(OFFERS));
    }

    /**
     * Add a WHERE close ensuring that the demands have at least one contribution.
     */
    public void hasContributions() {
        add(Restrictions.isNotEmpty(CONTRIBUTION));
    }

    /**
     * Add a WHERE close ensuring that the demands have no contribution.
     */
    public void hasNoContribution() {
        add(Restrictions.isEmpty(CONTRIBUTION));
    }

    /**
     * Add a WHERE close restricting the total contribution value of the returning
     * demands. For example if you want your query to return only the demands that have
     * less than 42 €, you can call:
     * 
     * <pre>
     * DaoDemandListFactory factory = new DaoDemandListfactory();
     * factory.contribution(Comparator.LESS, 42);
     * PageIterable&lt;DaoDemand&gt; demands = factory.createCollection();
     * </pre>
     * 
     * @param cmp the cmp.
     * @param value the value
     */
    public void contribution(Comparator cmp, BigDecimal value) {
        add(createNbCriterion(cmp, CONTRIBUTION, value));
    }

}
