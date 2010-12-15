package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IdeaPageUrl extends Url {
    public IdeaPageUrl() {
        super("IdeaPage");
    }

    public IdeaPageUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private com.bloatit.framework.Demand idea;
    private IdeaTabPaneUrl demandTabPaneUrl = new IdeaTabPaneUrl();

    public com.bloatit.framework.Demand getIdea() {
        return this.idea;
    }

    public void setIdea(final com.bloatit.framework.Demand arg0) {
        this.idea = arg0;
    }

    public java.lang.String getTitle() {
        if (idea != null) {
            return idea.getTitle();
        } else {
            return null;
        }
    }

    public IdeaTabPaneUrl getDemandTabPaneUrl() {
        return this.demandTabPaneUrl;
    }

    public void setDemandTabPaneUrl(final IdeaTabPaneUrl arg0) {
        this.demandTabPaneUrl = arg0;
    }

    @Override
    protected void doRegister() {
        register(new Parameter("id", getIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(new Parameter("title", getTitle(), java.lang.String.class, Role.PRETTY, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(demandTabPaneUrl);
    }

    @Override
    public IdeaPageUrl clone() {
        final IdeaPageUrl other = new IdeaPageUrl();
        other.idea = this.idea;
        other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
        return other;
    }
}
