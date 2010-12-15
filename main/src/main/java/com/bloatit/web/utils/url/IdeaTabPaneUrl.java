package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IdeaTabPaneUrl extends UrlComponent {
    public IdeaTabPaneUrl() {
        super();
    }

    public IdeaTabPaneUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private java.lang.String activeTabKey;
    private IdeaContributorsComponentUrl contributionUrl = new IdeaContributorsComponentUrl();

    public java.lang.String getActiveTabKey() {
        return this.activeTabKey;
    }

    public void setActiveTabKey(final java.lang.String arg0) {
        this.activeTabKey = arg0;
    }

    public IdeaContributorsComponentUrl getContributionUrl() {
        return this.contributionUrl;
    }

    public void setContributionUrl(final IdeaContributorsComponentUrl arg0) {
        this.contributionUrl = arg0;
    }

    @Override
    protected void doRegister() {
        register(new Parameter("demand_tab_key", getActiveTabKey(), java.lang.String.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(contributionUrl);
    }

    @Override
    public IdeaTabPaneUrl clone() {
        final IdeaTabPaneUrl other = new IdeaTabPaneUrl();
        other.activeTabKey = this.activeTabKey;
        other.contributionUrl = this.contributionUrl.clone();
        return other;
    }
}
