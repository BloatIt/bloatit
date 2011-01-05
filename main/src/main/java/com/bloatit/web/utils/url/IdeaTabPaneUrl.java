package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.ConversionErrorException;

@SuppressWarnings("unused")
public final class IdeaTabPaneUrl extends UrlComponent {
public IdeaTabPaneUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public IdeaTabPaneUrl(){
    super();
    try {
        this.activeTabKey.setValue(Loaders.fromStr(java.lang.String.class, "description_tab"));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<java.lang.String> activeTabKey =     new UrlParameter<java.lang.String>("demand_tab_key", null, java.lang.String.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private IdeaContributorsComponentUrl contributionUrl = new IdeaContributorsComponentUrl();

public java.lang.String getActiveTabKey(){ 
    return this.activeTabKey.getValue();
}

public void setActiveTabKey(java.lang.String arg){ 
    this.activeTabKey.setValue(arg);
}

public IdeaContributorsComponentUrl getContributionUrl(){ 
    return this.contributionUrl;
}

public void setContributionUrl(IdeaContributorsComponentUrl arg){ 
    this.contributionUrl = arg;
}


@Override 
protected void doRegister() { 
    register(activeTabKey);
    register(contributionUrl);
}

@Override 
public IdeaTabPaneUrl clone() { 
    IdeaTabPaneUrl other = new IdeaTabPaneUrl();
    other.activeTabKey = this.activeTabKey.clone();
    other.contributionUrl = this.contributionUrl.clone();
    return other;
}
}
