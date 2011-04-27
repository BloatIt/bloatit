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
package com.bloatit.model.admin;

import java.math.BigDecimal;

import com.bloatit.data.DaoMilestone;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.data.queries.DaoMilestoneQuery;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.Milestone;
import com.bloatit.model.lists.MilestoneList;

public class MilestoneAdminListFactory extends IdentifiableAdminListFactory<DaoMilestone, Milestone> {

    public MilestoneAdminListFactory() {
        super(new DaoMilestoneQuery());
    }

    @Override
    protected DaoMilestoneQuery getfactory() {
        return (DaoMilestoneQuery) super.getfactory();
    }

    @Override
    public PageIterable<Milestone> list() {
        return new MilestoneList(getfactory().createCollection());
    }

    public void amount(final Comparator cmp, final BigDecimal value) {
        getfactory().amount(cmp, value);
    }

    public void stateEquals(final MilestoneState state) {
        getfactory().stateEquals(state);
    }

    public void withRelease() {
        getfactory().withRelease();
    }

    public void withoutRelease() {
        getfactory().withoutRelease();
    }

}
