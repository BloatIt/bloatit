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
private com.bloatit.framework.Demand targetDemand;
private com.bloatit.framework.Demand idea;
private java.lang.String comment;
private java.math.BigDecimal amount;

public com.bloatit.framework.Demand getTargetDemand(){ 
    return this.targetDemand;
}

public void setTargetDemand(com.bloatit.framework.Demand arg0){ 
    this.targetDemand = arg0;
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
    register(new Parameter("bloatit_target_demand", getTargetDemand(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("idea", getIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("bloatit_comment", getComment(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("bloatit_contribute", getAmount(), java.math.BigDecimal.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public ContributionActionUrl clone() { 
    ContributionActionUrl other = new ContributionActionUrl();
    other.targetDemand = this.targetDemand;
    other.idea = this.idea;
    other.comment = this.comment;
    other.amount = this.amount;
    return other;
}
}
