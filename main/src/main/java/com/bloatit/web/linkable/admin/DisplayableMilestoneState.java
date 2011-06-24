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
package com.bloatit.web.linkable.admin;

import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableMilestoneState implements Displayable {
    NOT_SELECTED(tr("<select>")), //
    PENDING(tr("Pending")), //
    DEVELOPING(tr("Developing")), //
    UAT(tr("Released")), //
    VALIDATED(tr("Validated")), //
    CANCELED(tr("Canceled"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return Context.tr(displayName);
    }

    private DisplayableMilestoneState(final String displayName) {
        this.displayName = displayName;
    }

    public static DisplayableMilestoneState getState(final MilestoneState cmp) {
        return Enum.valueOf(DisplayableMilestoneState.class, cmp.name());
    }

    protected static MilestoneState getState(final DisplayableMilestoneState cmp) {
        return Enum.valueOf(MilestoneState.class, cmp.name());
    }

    // Fake tr
    private static String tr(String fake) {
        return fake;
    }
}
