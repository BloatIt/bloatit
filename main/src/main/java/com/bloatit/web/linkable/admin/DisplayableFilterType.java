package com.bloatit.web.linkable.admin;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.form.Displayable;

public enum DisplayableFilterType implements Displayable {
    NO_FILTER(tr("No filter")), //
    WITH(tr("With")), //
    WITHOUT(tr("Without"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableFilterType(final String displayName) {
        this.displayName = displayName;
    }
}
