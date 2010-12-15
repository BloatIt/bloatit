package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class ContributionActionUrl extends Url {
public ContributionActionUrl() {
    super("ContributionAction"); 
}
public ContributionActionUrl(Parameters params) {
    this();
    parseParameters(params);
}
private com.bloatit.framework.Demand targetIdea;
private java.lang.String comment;
private java.math.BigDecimal amount;

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.framework.Demand arg0){ 
    this.targetIdea = arg0;
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
    register(new Parameter("targetIdea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("comment", getComment(), java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("contributionAmount", getAmount(), java.math.BigDecimal.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public ContributionActionUrl clone() { 
    ContributionActionUrl other = new ContributionActionUrl();
    other.targetIdea = this.targetIdea;
    other.comment = this.comment;
    other.amount = this.amount;
    return other;
}
}
