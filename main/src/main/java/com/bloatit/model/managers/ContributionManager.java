/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.model.managers;

import java.math.BigDecimal;

import com.bloatit.data.DaoContribution;
import com.bloatit.data.queries.DBRequests;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.Member;
import com.bloatit.model.lists.ContributionList;

/**
 *
 */
public class ContributionManager {
    private ContributionManager() {
        // Disable ctor
    }

    /**
     * Gets a Comment by id.
     * 
     * @param id the {@link Comment} id
     * @return the Comment or null if not found.
     */
    public static Contribution getById(final Integer id) {
        return Contribution.create(DBRequests.getById(DaoContribution.class, id));
    }

    public static ContributionList getAll() {
        return new ContributionList(DBRequests.getAll(DaoContribution.class));
    }

    public static Contribution getByFeatureMember(FeatureImplementation f, Member m) {
        return Contribution.create(DaoContribution.getByFeatureMember(f.getDao(), m.getDao()));
    }

    public static BigDecimal getMoneyRaised() {
        return DaoContribution.getMoneyRaised();
    }
}
