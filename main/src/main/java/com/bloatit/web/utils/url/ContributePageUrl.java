package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;

@SuppressWarnings("unused")
public class ContributePageUrl extends Url {
    public static String getName() {
        return "contribute";
    }

    @Override
    public com.bloatit.web.html.pages.ContributePage createPage() throws RedirectException {
        return new com.bloatit.web.html.pages.ContributePage(this);
    }

    public ContributePageUrl(final Parameters params, final Parameters session) {
        this();
        parseParameters(params, false);
        parseParameters(session, true);
    }

    public ContributePageUrl(final com.bloatit.framework.Demand targetIdea) {
        this();
        this.targetIdea.setValue(targetIdea);
    }

    private ContributePageUrl() {
        super(getName());
        try {
            this.contributionAmountParam.setValue(Loaders.fromStr(java.lang.String.class, ""));
            this.contributionCommentParam.setValue(Loaders.fromStr(java.lang.String.class, ""));
        } catch (final ConversionErrorException e) {
            e.printStackTrace();
            assert false;
        }
    }

    private UrlParameter<com.bloatit.framework.Demand> targetIdea = new UrlParameter<com.bloatit.framework.Demand>("targetIdea", null,
            com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<java.lang.String> contributionAmountParam = new UrlParameter<java.lang.String>("contributionAmount", null,
            java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
    private UrlParameter<java.lang.String> contributionCommentParam = new UrlParameter<java.lang.String>("comment", null, java.lang.String.class,
            Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

    public com.bloatit.framework.Demand getTargetIdea() {
        return this.targetIdea.getValue();
    }

    public void setTargetIdea(final com.bloatit.framework.Demand arg) {
        this.targetIdea.setValue(arg);
    }

    public java.lang.String getContributionAmountParam() {
        return this.contributionAmountParam.getValue();
    }

    public void setContributionAmountParam(final java.lang.String arg) {
        this.contributionAmountParam.setValue(arg);
    }

    public java.lang.String getContributionCommentParam() {
        return this.contributionCommentParam.getValue();
    }

    public void setContributionCommentParam(final java.lang.String arg) {
        this.contributionCommentParam.setValue(arg);
    }

    @Override
    protected void doRegister() {
        register(targetIdea);
        register(contributionAmountParam);
        register(contributionCommentParam);
    }

    @Override
    public ContributePageUrl clone() {
        final ContributePageUrl other = new ContributePageUrl();
        other.targetIdea = this.targetIdea.clone();
        other.contributionAmountParam = this.contributionAmountParam.clone();
        other.contributionCommentParam = this.contributionCommentParam.clone();
        return other;
    }
}
