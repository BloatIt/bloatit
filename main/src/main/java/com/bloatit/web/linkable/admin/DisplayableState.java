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

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableState implements Displayable {
    NO_FILTER(tr("<select>")), //
    VALIDATED(tr("Validate")), //
    PENDING(tr("Pending")), //
    HIDDEN(tr("Hidden")), //
    REJECTED(tr("Rejected"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return Context.tr(displayName);
    }

    private DisplayableState(final String displayName) {
        this.displayName = displayName;
    }

    protected static PopularityState getState(final DisplayableState cmp) {
        return Enum.valueOf(PopularityState.class, cmp.name());
    }

    // Fake tr
    private static String tr(final String fake) {
        return fake;
    }
}
