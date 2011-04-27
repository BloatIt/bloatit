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

    //Fake tr
    private static String tr(final String fake) {
        return fake;
    }
}
