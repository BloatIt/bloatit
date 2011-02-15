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
public final class PaylineActionUrlComponent extends UrlComponent {
public PaylineActionUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public PaylineActionUrlComponent(){
    super();
}
private UrlParameter<java.math.BigDecimal, java.math.BigDecimal> amount =  new UrlParameter<java.math.BigDecimal, java.math.BigDecimal>(null, new UrlParameterDescription<java.math.BigDecimal>("amount", java.math.BigDecimal.class, Role.POST, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.math.BigDecimal>());

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
    register(amount);
}

@Override 
public PaylineActionUrlComponent clone() { 
    PaylineActionUrlComponent other = new PaylineActionUrlComponent();
    other.amount = this.amount.clone();
    return other;
}
}
