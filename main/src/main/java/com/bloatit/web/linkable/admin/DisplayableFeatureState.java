package com.bloatit.web.linkable.admin;

import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableFeatureState implements Displayable {
    NO_FILTER(tr("<not selected>")), //
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
        return Context.tr(displayName);
    }

    private DisplayableFeatureState(final String displayName) {
        this.displayName = displayName;
    }

    protected static FeatureState getFeatureState(final DisplayableFeatureState cmp) {
        return Enum.valueOf(FeatureState.class, cmp.name());
    }

    public static DisplayableFeatureState getFeatureState(final FeatureState cmp) {
        return Enum.valueOf(DisplayableFeatureState.class, cmp.name());
    }

    //Fake tr
    private static String tr(final String fake) {
        return fake;
    }
}
