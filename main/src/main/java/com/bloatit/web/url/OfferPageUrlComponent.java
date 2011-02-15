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
public final class OfferPageUrlComponent extends UrlComponent {
public OfferPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public OfferPageUrlComponent(com.bloatit.model.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private OfferPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> targetIdea =  new UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand>(null, new UrlParameterDescription<com.bloatit.model.Demand>("targetIdea", com.bloatit.model.Demand.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Demand>());
private UrlParameter<java.math.BigDecimal, java.math.BigDecimal> price =  new UrlParameter<java.math.BigDecimal, java.math.BigDecimal>(null, new UrlParameterDescription<java.math.BigDecimal>("offer_price", java.math.BigDecimal.class, Role.SESSION, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<java.math.BigDecimal>());
private UrlParameter<com.bloatit.framework.utils.i18n.DateLocale, com.bloatit.framework.utils.i18n.DateLocale> expiryDate =  new UrlParameter<com.bloatit.framework.utils.i18n.DateLocale, com.bloatit.framework.utils.i18n.DateLocale>(null, new UrlParameterDescription<com.bloatit.framework.utils.i18n.DateLocale>("offer_expiry", com.bloatit.framework.utils.i18n.DateLocale.class, Role.SESSION, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<com.bloatit.framework.utils.i18n.DateLocale>());
private UrlParameter<java.lang.String, java.lang.String> title =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("offer_title", java.lang.String.class, Role.SESSION, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<java.lang.String>());
private UrlParameter<java.lang.String, java.lang.String> description =  new UrlParameter<java.lang.String, java.lang.String>(null, new UrlParameterDescription<java.lang.String>("offer_description", java.lang.String.class, Role.SESSION, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.INFO), new UrlParameterConstraints<java.lang.String>());

public com.bloatit.model.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public UrlParameter<com.bloatit.model.Demand, com.bloatit.model.Demand> getTargetIdeaParameter(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.model.Demand arg){ 
    this.targetIdea.setValue(arg);
}

public java.math.BigDecimal getPrice(){ 
    return this.price.getValue();
}

public UrlParameter<java.math.BigDecimal, java.math.BigDecimal> getPriceParameter(){ 
    return this.price;
}

public void setPrice(java.math.BigDecimal arg){ 
    this.price.setValue(arg);
}

public com.bloatit.framework.utils.i18n.DateLocale getExpiryDate(){ 
    return this.expiryDate.getValue();
}

public UrlParameter<com.bloatit.framework.utils.i18n.DateLocale, com.bloatit.framework.utils.i18n.DateLocale> getExpiryDateParameter(){ 
    return this.expiryDate;
}

public void setExpiryDate(com.bloatit.framework.utils.i18n.DateLocale arg){ 
    this.expiryDate.setValue(arg);
}

public java.lang.String getTitle(){ 
    return this.title.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getTitleParameter(){ 
    return this.title;
}

public void setTitle(java.lang.String arg){ 
    this.title.setValue(arg);
}

public java.lang.String getDescription(){ 
    return this.description.getValue();
}

public UrlParameter<java.lang.String, java.lang.String> getDescriptionParameter(){ 
    return this.description;
}

public void setDescription(java.lang.String arg){ 
    this.description.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(targetIdea);
    register(price);
    register(expiryDate);
    register(title);
    register(description);
}

@Override 
public OfferPageUrlComponent clone() { 
    OfferPageUrlComponent other = new OfferPageUrlComponent();
    other.targetIdea = this.targetIdea.clone();
    other.price = this.price.clone();
    other.expiryDate = this.expiryDate.clone();
    other.title = this.title.clone();
    other.description = this.description.clone();
    return other;
}
}
