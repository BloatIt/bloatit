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

import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableComparator implements Displayable {
    EQUAL(tr("Equal")), //
    LESS(tr("Less than")), //
    GREATER(tr("Greter than")), //
    LESS_EQUAL(tr("Less or equal")), //
    GREATER_EQUAL(tr("Greater or equal"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return Context.tr(displayName);
    }

    private DisplayableComparator(final String displayName) {
        this.displayName = displayName;
    }

    public static DisplayableComparator getComparator(final Comparator cmp) {
        return Enum.valueOf(DisplayableComparator.class, cmp.name());
    }

    protected static Comparator getComparator(final DisplayableComparator cmp) {
        return Enum.valueOf(Comparator.class, cmp.name());
    }

    //Fake tr
    private static String tr(String fake) {
        return fake;
    }
}
