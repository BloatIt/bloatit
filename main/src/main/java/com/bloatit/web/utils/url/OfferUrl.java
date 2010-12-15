package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class OfferUrl extends Url {
public OfferUrl() {
    super("offer"); 
}
public OfferUrl(Parameters params) {
    this();
    parseParameters(params);
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

public OfferUrl clone() { 
    OfferUrl other = new OfferUrl();
    other.targetIdea = this.targetIdea;
    other.price = this.price;
    other.expiryDate = this.expiryDate;
    other.title = this.title;
    other.description = this.description;
    return other;
}
}
