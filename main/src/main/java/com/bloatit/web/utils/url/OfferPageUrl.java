package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class OfferPageUrl extends Url {
public static String getName() { return "OfferPage"; }
public com.bloatit.web.html.pages.OfferPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.OfferPage(this); }
public OfferPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public OfferPageUrl(com.bloatit.framework.Demand targetIdea) {
    super(getName());
    try {
        this.price = Loaders.fromStr(java.math.BigDecimal.class, "");
        this.expiryDate = Loaders.fromStr(java.util.Date.class, "");
        this.title = Loaders.fromStr(java.lang.String.class, "");
        this.description = Loaders.fromStr(java.lang.String.class, "");
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
        this.targetIdea = targetIdea;
}
private OfferPageUrl(){
    super(getName());
}
private com.bloatit.framework.Demand targetIdea;
private java.math.BigDecimal price;
private java.util.Date expiryDate;
private java.lang.String title;
private java.lang.String description;

public com.bloatit.framework.Demand getTargetIdea(){ 
    return this.targetIdea;
}

public void setTargetIdea(com.bloatit.framework.Demand arg0){ 
    this.targetIdea = arg0;
}

public java.math.BigDecimal getPrice(){ 
    return this.price;
}

public void setPrice(java.math.BigDecimal arg0){ 
    this.price = arg0;
}

public java.util.Date getExpiryDate(){ 
    return this.expiryDate;
}

public void setExpiryDate(java.util.Date arg0){ 
    this.expiryDate = arg0;
}

public java.lang.String getTitle(){ 
    return this.title;
}

public void setTitle(java.lang.String arg0){ 
    this.title = arg0;
}

public java.lang.String getDescription(){ 
    return this.description;
}

public void setDescription(java.lang.String arg0){ 
    this.description = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("idea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("price", getPrice(), java.math.BigDecimal.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("expiry", getExpiryDate(), java.util.Date.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("title", getTitle(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("description", getDescription(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public OfferPageUrl clone() { 
    OfferPageUrl other = new OfferPageUrl();
    other.targetIdea = this.targetIdea;
    other.price = this.price;
    other.expiryDate = this.expiryDate;
    other.title = this.title;
    other.description = this.description;
    return other;
}
}
