package com.bloatit.web.utils.url;

import java.util.Map;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class DemandUrl extends Url {
public DemandUrl() {
    super("demand"); 
}
public DemandUrl(Map<String, String> params) {
    super("demand"); 
    parseParameterMap(params);
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
protected void doRegister(Messages messages) { 
    register(new Parameter(messages, "id", getDemand(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter(messages, "title", getTitle(), java.lang.String.class, Role.PRETTY, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(demandTabPaneUrl);
}
}
