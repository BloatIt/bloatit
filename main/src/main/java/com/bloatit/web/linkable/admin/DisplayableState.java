package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.Context.tr;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.webprocessor.components.form.Displayable;

public enum DisplayableState implements Displayable {
    NO_FILTER(tr("No filter")), //
    VALIDATED(tr("Validate")), //
    PENDING(tr("Pending")), //
    HIDDEN(tr("Hidden")), //
    REJECTED(tr("Rejected"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableState(final String displayName) {
        this.displayName = displayName;
    }

    public static PopularityState getState(final DisplayableState cmp) {
        return Enum.valueOf(PopularityState.class, cmp.name());
    }
}
