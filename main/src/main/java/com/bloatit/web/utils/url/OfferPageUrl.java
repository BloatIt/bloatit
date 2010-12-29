package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class OfferPageUrl extends Url {
public static String getName() { return "offer"; }
public com.bloatit.web.html.pages.OfferPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.OfferPage(this); }
public OfferPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public OfferPageUrl(com.bloatit.framework.Demand targetIdea) {
    this();
        this.targetIdea.setValue(targetIdea);
}
private OfferPageUrl(){
    super(getName());
    try {
        this.price.setValue(Loaders.fromStr(java.math.BigDecimal.class, ""));
        this.expiryDate.setValue(Loaders.fromStr(com.bloatit.web.utils.DateLocale.class, ""));
        this.title.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.description.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<com.bloatit.framework.Demand> targetIdea =     new UrlParameter<com.bloatit.framework.Demand>("targetIdea", null, com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.math.BigDecimal> price =     new UrlParameter<java.math.BigDecimal>("offer_price", null, java.math.BigDecimal.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<com.bloatit.web.utils.DateLocale> expiryDate =     new UrlParameter<com.bloatit.web.utils.DateLocale>("offer_expiry", null, com.bloatit.web.utils.DateLocale.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> title =     new UrlParameter<java.lang.String>("offer_title", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> description =     new UrlParameter<java.lang.String>("offer_description", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

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

public com.bloatit.web.utils.DateLocale getExpiryDate(){ 
    return this.expiryDate.getValue();
}

public void setExpiryDate(com.bloatit.web.utils.DateLocale arg){ 
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
public OfferPageUrl clone() { 
    OfferPageUrl other = new OfferPageUrl();
    other.targetIdea = this.targetIdea.clone();
    other.price = this.price.clone();
    other.expiryDate = this.expiryDate.clone();
    other.title = this.title.clone();
    other.description = this.description.clone();
    return other;
}
}
