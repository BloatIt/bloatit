package com.bloatit.web.linkable.admin;


import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableState implements Displayable {
    NO_FILTER(tr("No filter")), //
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

    public static PopularityState getState(final DisplayableState cmp) {
        return Enum.valueOf(PopularityState.class, cmp.name());
    }

    //Fake tr
    private static String tr(String fake) {
        return fake;
    }
}
