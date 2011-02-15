package com.bloatit.web.url;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.*;
import com.bloatit.framework.webserver.url.*;
import com.bloatit.framework.webserver.url.Loaders.*;

@SuppressWarnings("unused")
public final class ContributionActionUrlComponent extends UrlComponent {
public ContributionActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public ContributionActionUrlComponent(com.bloatit.model.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private ContributionActionUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> targetIdea =  new UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand>(null, new UrlParameterDescription<com.bloatit.model.Demand>("targetIdea", com.bloatit.model.Demand.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());
private UrlParameter<java.lang.String, java.lang.String> comment =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("comment", java.lang.String.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.String>(-2147483648, false, 140, false, true, 2147483647, 2147483647, ParamConstraint.DEFAULT_ERROR_MSG, "Your comment is too long. It must be less than 140 char long.", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));
private UrlParameter<java.math.BigDecimal, java.math.BigDecimal> amount =  new UrlParameter<java.math.BigDecimal, java.math.BigDecimal>(null, new UrlParameterDescription<java.math.BigDecimal>("contributionAmount", java.math.BigDecimal.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.math.BigDecimal>(0, true, 1000000000, false, false, 0, 2147483647, "Amount must be superior to 0.", "We cannot accept such a generous offer!", ParamConstraint.DEFAULT_ERROR_MSG, "Please do not use Cents.", ParamConstraint.DEFAULT_ERROR_MSG));

public com.bloatit.model.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> getTargetIdeaParameter(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.model.Demand arg){ 
    this.targetIdea.setValue(arg);
}

public java.lang.String getComment(){ 
    return this.comment.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getCommentParameter(){ 
    return this.comment;
}

public void setComment(java.lang.String arg){ 
    this.comment.setValue(arg);
}

public java.math.BigDecimal getAmount(){ 
    return this.amount.getValue();
}

public UrlParameter<java.math.BigDecimal, java.math.BigDecimal> getAmountParameter(){ 
    return this.amount;
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

@Override 
public ContributionActionUrlComponent clone() { 
    ContributionActionUrlComponent other = new ContributionActionUrlComponent();
    other.targetIdea = this.targetIdea.clone();
    other.comment = this.comment.clone();
    other.amount = this.amount.clone();
    return other;
}
}
