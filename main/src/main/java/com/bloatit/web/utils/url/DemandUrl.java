package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class DemandUrl extends Url {
public DemandUrl() {
    super("Demand"); 
}
public DemandUrl(Parameters params) {
    this();
    parseParameters(params);
}
private com.bloatit.framework.Demand demand;
private DemandTabPaneUrl demandTabPaneUrl = new DemandTabPaneUrl();

public com.bloatit.framework.Demand getDemand(){ 
    return this.demand;
}

public void setDemand(com.bloatit.framework.Demand arg0){ 
    this.demand = arg0;
}

public java.lang.String getTitle(){ 
    if (demand != null) {
        return demand.getTitle();
    } else {
        return null;
    }
}

public DemandTabPaneUrl getDemandTabPaneUrl(){ 
    return this.demandTabPaneUrl;
}

public void setDemandTabPaneUrl(DemandTabPaneUrl arg0){ 
    this.demandTabPaneUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("id", getDemand(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("title", getTitle(), java.lang.String.class, Role.PRETTY, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(demandTabPaneUrl);
}

public DemandUrl clone() { 
    DemandUrl other = new DemandUrl();
    other.demand = this.demand;
    other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
    return other;
}
}
