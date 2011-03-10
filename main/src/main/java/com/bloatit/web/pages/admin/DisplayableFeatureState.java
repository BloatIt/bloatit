package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.webserver.components.form.Displayable;

public enum DisplayableFeatureState implements Displayable {
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

    private DisplayableFeatureState(final String displayName) {
        this.displayName = displayName;
    }

    public static FeatureState getFeatureState(final DisplayableFeatureState cmp) {
        return Enum.valueOf(FeatureState.class, cmp.name());
    }

    public static DisplayableFeatureState getFeatureState(final FeatureState cmp) {
        return Enum.valueOf(DisplayableFeatureState.class, cmp.name());
    }
}
