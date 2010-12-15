package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class DemandTabPaneUrl extends UrlComponent {
public DemandTabPaneUrl() {
    super(); 
}
public DemandTabPaneUrl(Parameters params) {
    this();
    parseParameters(params);
}
private java.lang.String activeTabKey;
private DemandContributorsComponentUrl contributionUrl = new DemandContributorsComponentUrl();

public java.lang.String getActiveTabKey(){ 
    return this.activeTabKey;
}

public void setActiveTabKey(java.lang.String arg0){ 
    this.activeTabKey = arg0;
}

public DemandContributorsComponentUrl getContributionUrl(){ 
    return this.contributionUrl;
}

public void setContributionUrl(DemandContributorsComponentUrl arg0){ 
    this.contributionUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("demand_tab_key", getActiveTabKey(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(contributionUrl);
}

public DemandTabPaneUrl clone() { 
    DemandTabPaneUrl other = new DemandTabPaneUrl();
    other.activeTabKey = this.activeTabKey;
    other.contributionUrl = this.contributionUrl.clone();
    return other;
}
}
