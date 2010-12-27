package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class IdeaPageUrl extends Url {
    public static String getName() {
        return "idea";
    }

    @Override
    public com.bloatit.web.html.pages.idea.IdeaPage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.idea.IdeaPage(this);
    }

    public IdeaPageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public IdeaPageUrl(final com.bloatit.framework.Demand idea) {
        this();
        this.idea.setValue(idea);
    }

    private IdeaPageUrl() {
        super(getName());
    }

    private UrlParameter<com.bloatit.framework.Demand> idea = new UrlParameter<com.bloatit.framework.Demand>("id", null,
            com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private IdeaTabPaneUrl demandTabPaneUrl = new IdeaTabPaneUrl();

    public com.bloatit.framework.Demand getIdea() {
        return this.idea.getValue();
    }

    public void setIdea(final com.bloatit.framework.Demand arg) {
        this.idea.setValue(arg);
    }

    public java.lang.String getTitle() {
        if (idea.getValue() != null) {
            return idea.getValue().getTitle();
        } else {
            return null;
        }
    }

    public IdeaTabPaneUrl getDemandTabPaneUrl() {
        return this.demandTabPaneUrl;
    }

    public void setDemandTabPaneUrl(final IdeaTabPaneUrl arg) {
        this.demandTabPaneUrl = arg;
    }

    @Override
    protected void doRegister() {
        register(idea);
        register(demandTabPaneUrl);
    }

    @Override
    public IdeaPageUrl clone() {
        final IdeaPageUrl other = new IdeaPageUrl();
        other.idea = this.idea.clone();
        other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
        return other;
    }
}
