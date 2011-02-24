package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.queries.DaoAbstractListFactory.Comparator;
import com.bloatit.framework.webserver.components.form.Displayable;

public enum DisplayableBatchState implements Displayable {
    NOT_SELECTED(tr("<select>")), //
    PENDING(tr("Pending")), //
    DEVELOPING(tr("Developing")), //
    UAT(tr("Released")), //
    VALIDATED(tr("Validated")), //
    CANCELED(tr("Canceled"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableBatchState(final String displayName) {
        this.displayName = displayName;
    }

    public static DisplayableBatchState getComparator(final Comparator cmp) {
        return Enum.valueOf(DisplayableBatchState.class, cmp.name());
    }

    public static Comparator getComparator(final DisplayableBatchState cmp) {
        return Enum.valueOf(Comparator.class, cmp.name());
    }
}
