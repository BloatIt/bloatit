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

    //Fake tr
    private static String tr(String fake) {
        return fake;
    }
}
