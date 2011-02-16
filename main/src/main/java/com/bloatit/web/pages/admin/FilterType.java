package com.bloatit.web.pages.admin;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.webserver.components.form.HtmlRadioButtonGroup;

public enum FilterType implements HtmlRadioButtonGroup.Displayable {
    NO_FILTER(tr("No filter")), //
    WITH(tr("Yes")), //
    WITHOUT(tr("No"));

    private String displayName;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    private FilterType(String displayName) {
        this.displayName = displayName;
    }
}