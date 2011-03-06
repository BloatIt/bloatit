package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.queries.DaoAbstractQuery.Comparator;
import com.bloatit.framework.webserver.components.form.Displayable;

public enum DisplayableComparator implements Displayable {
    EQUAL(tr("Equal")), //
    LESS(tr("Less than")), //
    GREATER(tr("Greter than")), //
    LESS_EQUAL(tr("Less or equal")), //
    GREATER_EQUAL(tr("Greater or equal"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableComparator(final String displayName) {
        this.displayName = displayName;
    }

    public static DisplayableComparator getComparator(final Comparator cmp) {
        return Enum.valueOf(DisplayableComparator.class, cmp.name());
    }

    public static Comparator getComparator(final DisplayableComparator cmp) {
        return Enum.valueOf(Comparator.class, cmp.name());
    }
}
