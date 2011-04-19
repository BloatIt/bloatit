package com.bloatit.web.linkable.admin;


import com.bloatit.framework.webprocessor.components.form.Displayable;
import com.bloatit.framework.webprocessor.context.Context;

public enum DisplayableFilterType implements Displayable {
    NO_FILTER(tr("No filter")), //
    WITH(tr("With")), //
    WITHOUT(tr("Without"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return Context.tr(displayName);
    }

    private DisplayableFilterType(final String displayName) {
        this.displayName = displayName;
    }

    //Fake tr
    private static String tr(String fake) {
        return fake;
    }
}
