package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
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
        this.price.setValue(Loaders.fromStr(java.math.BigDecimal.class, ""));
        this.expiryDate.setValue(Loaders.fromStr(java.util.Date.class, ""));
        this.title.setValue(Loaders.fromStr(java.lang.String.class, ""));
        this.description.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
        this.targetIdea.setValue(targetIdea);
}
private OfferPageUrl(){
    super(getName());
}
private Parameter<com.bloatit.framework.Demand> targetIdea =     new Parameter<com.bloatit.framework.Demand>("targetIdea", getTargetIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.math.BigDecimal> price =     new Parameter<java.math.BigDecimal>("price", getPrice(), java.math.BigDecimal.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.util.Date> expiryDate =     new Parameter<java.util.Date>("expiryDate", getExpiryDate(), java.util.Date.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.lang.String> title =     new Parameter<java.lang.String>("title", getTitle(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private Parameter<java.lang.String> description =     new Parameter<java.lang.String>("description", getDescription(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

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

public java.util.Date getExpiryDate(){ 
    return this.expiryDate.getValue();
}

public void setExpiryDate(java.util.Date arg){ 
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
