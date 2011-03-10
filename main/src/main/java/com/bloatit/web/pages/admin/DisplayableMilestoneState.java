package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.framework.webserver.components.form.Displayable;

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
        return displayName;
    }

    private DisplayableMilestoneState(final String displayName) {
        this.displayName = displayName;
    }

    public static DisplayableMilestoneState getState(final MilestoneState cmp) {
        return Enum.valueOf(DisplayableMilestoneState.class, cmp.name());
    }

    public static MilestoneState getState(final DisplayableMilestoneState cmp) {
        return Enum.valueOf(MilestoneState.class, cmp.name());
    }
}
