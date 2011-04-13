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
package com.bloatit.data.queries;

import java.math.BigDecimal;

import org.hibernate.criterion.Restrictions;

import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.SessionManager;

/**
 * A Factory to create query on the DB and return list of DaoFeature.
 */
public class DaoFeatureQuery extends DaoKudosableQuery<DaoFeature> {

    /** The Constant CONTRIBUTION. */
    private static String CONTRIBUTION = "contribution";

    /** The Constant OFFERS. */
    private static String OFFERS = "offers";

    /** The Constant SELECTED_OFFER. */
    private static String SELECTED_OFFER = "selectedOffer";

    /** The Constant SOFTWARE. */
    private static String SOFTWARE = "software";

    /** The Constant FEATURE_STATE. */
    private static String FEATURE_STATE = "featureState";

    /**
     * Instantiates a new dao feature list factory.
     */
    public DaoFeatureQuery() {
        super(SessionManager.getSessionFactory().getCurrentSession().createCriteria(DaoFeature.class));
    }

    /**
     * Add a WHERE close with featureState = <code>state</code>.
     * 
     * @param state the state
     */
    public void stateEquals(final FeatureState state) {
        add(Restrictions.eq(FEATURE_STATE, state));
    }

    /**
     * Add a WHERE close with software = <code>software</code>.
     * 
     * @param software the software
     */
    public void softwareEquals(final DaoSoftware software) {
        add(Restrictions.eq(SOFTWARE, software));
    }

    /**
     * Add a WHERE close ensuring that there is a selected offer on each
     * returned daoFeature.
     */
    public void withSelectedOffer() {
        add(Restrictions.isNotNull(SELECTED_OFFER));
    }

    /**
     * Add a WHERE close ensuring that there isn't a selected offer on each
     * returned daoFeature.
     */
    public void withoutSelectedOffer() {
        add(Restrictions.isNull(SELECTED_OFFER));
    }

    /**
     * Add a WHERE close ensuring that the features have at least one offer.
     */
    public void withOffer() {
        add(Restrictions.isNotEmpty(OFFERS));
    }

    /**
     * Add a WHERE close ensuring that the features have no offer.
     */
    public void withoutOffer() {
        add(Restrictions.isEmpty(OFFERS));
    }

    /**
     * Add a WHERE close ensuring that the features have at least one
     * contribution.
     */
    public void hasContributions() {
        add(Restrictions.isNotEmpty(CONTRIBUTION));
    }

    /**
     * Add a WHERE close ensuring that the features have no contribution.
     */
    public void hasNoContribution() {
        add(Restrictions.isEmpty(CONTRIBUTION));
    }

    /**
     * Add a WHERE close restricting the total contribution value of the
     * returning features. For example if you want your query to return only the
     * features that have less than 42 €, you can call:
     * 
     * <pre>
     * DaoFeatureListFactory factory = new DaoFeatureListfactory();
     * factory.contribution(Comparator.LESS, 42);
     * PageIterable&lt;DaoFeature&gt; features = factory.createCollection();
     * </pre>
     * 
     * @param cmp the cmp.
     * @param value the value
     */
    public void contribution(final Comparator cmp, final BigDecimal value) {
        add(createNbCriterion(cmp, CONTRIBUTION, value));
    }

}
