package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class ContributePageUrl extends Url {
public static String getName() { return "contribute"; }
public com.bloatit.web.html.pages.ContributePage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.ContributePage(this); }
public ContributePageUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public ContributePageUrl(com.bloatit.framework.Demand targetIdea) {
    super(getName());
    try {
        this.contributionAmountParam.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.contributionCommentParam.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
        this.targetIdea.setValue(targetIdea);
}
private ContributePageUrl(){
    super(getName());
}
private Parameter<com.bloatit.framework.Demand> targetIdea =     new Parameter<com.bloatit.framework.Demand>("targetIdea", null, com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.lang.String> contributionAmountParam =     new Parameter<java.lang.String>("contributionAmountParam", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.lang.String> contributionCommentParam =     new Parameter<java.lang.String>("contributionCommentParam", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public void setTargetIdea(com.bloatit.framework.Demand arg){ 
    this.targetIdea.setValue(arg);
}

public java.lang.String getContributionAmountParam(){ 
    return this.contributionAmountParam.getValue();
}

public void setContributionAmountParam(java.lang.String arg){ 
    this.contributionAmountParam.setValue(arg);
}

public java.lang.String getContributionCommentParam(){ 
    return this.contributionCommentParam.getValue();
}

public void setContributionCommentParam(java.lang.String arg){ 
    this.contributionCommentParam.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetIdea);
    register(contributionAmountParam);
    register(contributionCommentParam);
}

public ContributePageUrl clone() { 
    ContributePageUrl other = new ContributePageUrl();
    other.targetIdea = this.targetIdea.clone();
    other.contributionAmountParam = this.contributionAmountParam.clone();
    other.contributionCommentParam = this.contributionCommentParam.clone();
    return other;
}
}
