package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class ContributePageUrl extends Url {
    public ContributePageUrl() {
        super("ContributePage");
    }

    public ContributePageUrl(final Parameters params) {
        this();
        parseParameters(params);
    }

    private com.bloatit.framework.Demand targetIdea;
    private java.lang.String contributionAmountParam;
    private java.lang.String contributionCommentParam;

    public com.bloatit.framework.Demand getTargetIdea() {
        return this.targetIdea;
    }

    public void setTargetIdea(final com.bloatit.framework.Demand arg0) {
        this.targetIdea = arg0;
    }

    public java.lang.String getContributionAmountParam() {
        return this.contributionAmountParam;
    }

    public void setContributionAmountParam(final java.lang.String arg0) {
        this.contributionAmountParam = arg0;
    }

    public java.lang.String getContributionCommentParam() {
        return this.contributionCommentParam;
    }

    public void setContributionCommentParam(final java.lang.String arg0) {
        this.contributionCommentParam = arg0;
    }

    @Override
    protected void doRegister() {
        register(new Parameter("targetIdea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(new Parameter("contributionAmountParam", getContributionAmountParam(), java.lang.String.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
        register(new Parameter("contributionCommentParam", getContributionCommentParam(), java.lang.String.class, Role.GET, Level.ERROR,
                "Error: invalid value (%value) for parameter \"%param\""));
    }

    @Override
    public ContributePageUrl clone() {
        final ContributePageUrl other = new ContributePageUrl();
        other.targetIdea = this.targetIdea;
        other.contributionAmountParam = this.contributionAmountParam;
        other.contributionCommentParam = this.contributionCommentParam;
        return other;
    }
}
