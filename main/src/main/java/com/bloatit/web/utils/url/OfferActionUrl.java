package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class OfferActionUrl extends Url {
public static String getName() { return "action/offer"; }
public com.bloatit.web.actions.OfferAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.OfferAction(this); }
public OfferActionUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public OfferActionUrl(com.bloatit.framework.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private OfferActionUrl(){
    super(getName());
}
private UrlParameter<com.bloatit.framework.Demand> targetIdea =     new UrlParameter<com.bloatit.framework.Demand>("targetIdea", null, com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.math.BigDecimal> price =     new UrlParameter<java.math.BigDecimal>("offer_price", null, java.math.BigDecimal.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<com.bloatit.web.utils.i18n.DateLocale> expiryDate =     new UrlParameter<com.bloatit.web.utils.i18n.DateLocale>("offer_expiry", null, com.bloatit.web.utils.i18n.DateLocale.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> title =     new UrlParameter<java.lang.String>("offer_title", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> description =     new UrlParameter<java.lang.String>("offer_description", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea.getValue();
}

public void setTargetIdea(com.bloatit.framework.Demand arg){ 
    this.targetIdea.setValue(arg);
}

public java.math.BigDecimal getPrice(){ 
    return this.price.getValue();
}

public void setPrice(java.math.BigDecimal arg){ 
    this.price.setValue(arg);
}

public com.bloatit.web.utils.i18n.DateLocale getExpiryDate(){ 
    return this.expiryDate.getValue();
}

public void setExpiryDate(com.bloatit.web.utils.i18n.DateLocale arg){ 
    this.expiryDate.setValue(arg);
}

public java.lang.String getTitle(){ 
    return this.title.getValue();
}

public void setTitle(java.lang.String arg){ 
    this.title.setValue(arg);
}

public java.lang.String getDescription(){ 
    return this.description.getValue();
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
public OfferActionUrl clone() { 
    OfferActionUrl other = new OfferActionUrl();
    other.targetIdea = this.targetIdea.clone();
    other.price = this.price.clone();
    other.expiryDate = this.expiryDate.clone();
    other.title = this.title.clone();
    other.description = this.description.clone();
    return other;
}
}
