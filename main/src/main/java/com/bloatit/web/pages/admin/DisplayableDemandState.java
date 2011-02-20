package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;

public enum DisplayableDemandState implements HtmlRadioButtonGroup.Displayable {
    NO_FILTER(tr("No filter")), //
    PENDING(tr("Validate")), //
    PREPARING(tr("Pending")), //
    DEVELOPPING(tr("Developing")), //
    INCOME(tr("Income")), //
    DISCARDED(tr("Discarded")), //
    FINISHED(tr("Finished")), //
    ;

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableDemandState(final String displayName) {
        this.displayName = displayName;
    }

    public static DemandState getDemandState(final DisplayableDemandState cmp) {
        return Enum.valueOf(DemandState.class, cmp.name());
    }

    public static DisplayableDemandState getDemandState(final DemandState cmp) {
        return Enum.valueOf(DisplayableDemandState.class, cmp.name());
    }
}
