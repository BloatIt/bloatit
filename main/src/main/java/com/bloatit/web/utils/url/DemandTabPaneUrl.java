package com.bloatit.web.utils.url;

import java.util.Map;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;

public class DemandTabPaneUrl extends UrlComponent {
public DemandTabPaneUrl() {
    super(); 
}
public DemandTabPaneUrl(Map<String, String> params) {
    super(); 
    parseParameterMap(params);
}
private java.lang.String activeTabKey;

public java.lang.String getActiveTabKey(){ 
    return this.activeTabKey;
}

public void setActiveTabKey(java.lang.String arg0){ 
    this.activeTabKey = arg0;
}


@Override 
protected void doRegister(Messages messages) { 
    register(new Parameter(messages, "demand_tab_key", getActiveTabKey(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}
}
