package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;

public enum DisplayableState implements HtmlRadioButtonGroup.Displayable {
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

    private DisplayableState(String displayName) {
        this.displayName = displayName;
    }

    public static PopularityState getState(DisplayableState cmp) {
        return Enum.valueOf(PopularityState.class, cmp.name());
    }
}