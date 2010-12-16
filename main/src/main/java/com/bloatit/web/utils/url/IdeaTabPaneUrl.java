package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class IdeaTabPaneUrl extends UrlComponent {
public IdeaTabPaneUrl(Parameters params) {
    super();
    parseParameters(params);
}
public IdeaTabPaneUrl() {
    super();
    try {
        this.activeTabKey = Loaders.fromStr(java.lang.String.class, "description_tab");
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private java.lang.String activeTabKey;
private IdeaContributorsComponentUrl contributionUrl = new IdeaContributorsComponentUrl();

public java.lang.String getActiveTabKey(){ 
    return this.activeTabKey;
}

public void setActiveTabKey(java.lang.String arg0){ 
    this.activeTabKey = arg0;
}

public IdeaContributorsComponentUrl getContributionUrl(){ 
    return this.contributionUrl;
}

public void setContributionUrl(IdeaContributorsComponentUrl arg0){ 
    this.contributionUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("demand_tab_key", getActiveTabKey(), java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(contributionUrl);
}

public IdeaTabPaneUrl clone() { 
    IdeaTabPaneUrl other = new IdeaTabPaneUrl();
    other.activeTabKey = this.activeTabKey;
    other.contributionUrl = this.contributionUrl.clone();
    return other;
}
}
