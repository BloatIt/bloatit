package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class ContributeUrl extends Url {
public ContributeUrl() {
    super("contribute"); 
}
public ContributeUrl(Parameters params) {
    this();
    parseParameters(params);
}
private com.bloatit.framework.Demand targetIdea;
private java.lang.String contributionAmountParam;
private java.lang.String contributionCommentParam;

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.framework.Demand arg0){ 
    this.targetIdea = arg0;
}

public java.lang.String getContributionAmountParam(){ 
    return this.contributionAmountParam;
}

public void setContributionAmountParam(java.lang.String arg0){ 
    this.contributionAmountParam = arg0;
}

public java.lang.String getContributionCommentParam(){ 
    return this.contributionCommentParam;
}

public void setContributionCommentParam(java.lang.String arg0){ 
    this.contributionCommentParam = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("targetIdea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("contributionAmountParam", getContributionAmountParam(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("contributionCommentParam", getContributionCommentParam(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public ContributeUrl clone() { 
    ContributeUrl other = new ContributeUrl();
    other.targetIdea = this.targetIdea;
    other.contributionAmountParam = this.contributionAmountParam;
    other.contributionCommentParam = this.contributionCommentParam;
    return other;
}
}
