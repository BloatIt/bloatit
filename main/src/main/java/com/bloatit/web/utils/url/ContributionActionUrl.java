package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class ContributionActionUrl extends Url {
public static String getName() { return "ContributionAction"; }
public com.bloatit.web.actions.ContributionAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.ContributionAction(this); }
public ContributionActionUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public ContributionActionUrl() {
    super(getName());
    try {
        this.comment = Loaders.fromStr(java.lang.String.class, "");
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private com.bloatit.framework.Demand targetIdea;
private com.bloatit.framework.Demand idea;
private java.lang.String comment;
private java.math.BigDecimal amount;

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.framework.Demand arg0){ 
    this.targetIdea = arg0;
}

public com.bloatit.framework.Demand getIdea(){ 
    return this.idea;
}

public void setIdea(com.bloatit.framework.Demand arg0){ 
    this.idea = arg0;
}

public java.lang.String getComment(){ 
    return this.comment;
}

public void setComment(java.lang.String arg0){ 
    this.comment = arg0;
}

public java.math.BigDecimal getAmount(){ 
    return this.amount;
}

public void setAmount(java.math.BigDecimal arg0){ 
    this.amount = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("targetIdea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("idea", getIdea(), com.bloatit.framework.Demand.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("comment", getComment(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("contributionAmount", getAmount(), java.math.BigDecimal.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public ContributionActionUrl clone() { 
    ContributionActionUrl other = new ContributionActionUrl();
    other.targetIdea = this.targetIdea;
    other.idea = this.idea;
    other.comment = this.comment;
    other.amount = this.amount;
    return other;
}
}
