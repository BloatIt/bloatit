package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class ContributionActionUrl extends Url {
public static String getName() { return "action/contribute"; }
public com.bloatit.web.actions.ContributionAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.ContributionAction(this); }
public ContributionActionUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public ContributionActionUrl(com.bloatit.framework.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private ContributionActionUrl(){
    super(getName());
}
private UrlParameter<com.bloatit.framework.Demand> targetIdea =     new UrlParameter<com.bloatit.framework.Demand>("targetIdea", null, com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> comment =     new UrlParameter<java.lang.String>("comment", null, java.lang.String.class, Role.SESSION, Level.INFO, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.math.BigDecimal> amount =     new UrlParameter<java.math.BigDecimal>("contributionAmount", null, java.math.BigDecimal.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public void setTargetIdea(com.bloatit.framework.Demand arg){ 
    this.targetIdea.setValue(arg);
}

public java.lang.String getComment(){ 
    return this.comment.getValue();
}

public void setComment(java.lang.String arg){ 
    this.comment.setValue(arg);
}

public java.math.BigDecimal getAmount(){ 
    return this.amount.getValue();
}

public void setAmount(java.math.BigDecimal arg){ 
    this.amount.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetIdea);
    register(comment);
    register(amount);
}

public ContributionActionUrl clone() { 
    ContributionActionUrl other = new ContributionActionUrl();
    other.targetIdea = this.targetIdea.clone();
    other.comment = this.comment.clone();
    other.amount = this.amount.clone();
    return other;
}
}
