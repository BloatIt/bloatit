package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;

public enum DisplayableFilterType implements HtmlRadioButtonGroup.Displayable {
    NO_FILTER(tr("No filter")), //
    WITH(tr("With")), //
    WITHOUT(tr("Without"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private DisplayableFilterType(String displayName) {
        this.displayName = displayName;
    }
}