package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class IdeaPageUrl extends Url {
public static String getName() { return "idea"; }
public com.bloatit.web.html.pages.idea.IdeaPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.idea.IdeaPage(this); }
public IdeaPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public IdeaPageUrl(com.bloatit.framework.Demand idea) {
    this();
        this.idea.setValue(idea);
}
private IdeaPageUrl(){
    super(getName());
}
private UrlParameter<com.bloatit.framework.Demand> idea =     new UrlParameter<com.bloatit.framework.Demand>("id", null, com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private IdeaTabPaneUrl demandTabPaneUrl = new IdeaTabPaneUrl();

public com.bloatit.framework.Demand getIdea(){ 
    return this.idea.getValue();
}

public void setIdea(com.bloatit.framework.Demand arg){ 
    this.idea.setValue(arg);
}

public java.lang.String getTitle(){ 
    if (idea.getValue() != null) {
        return idea.getValue().getTitle();
    } else {
        return null;
    }
}

public IdeaTabPaneUrl getDemandTabPaneUrl(){ 
    return this.demandTabPaneUrl;
}

public void setDemandTabPaneUrl(IdeaTabPaneUrl arg){ 
    this.demandTabPaneUrl = arg;
}


@Override 
protected void doRegister() { 
    register(idea);
    register(demandTabPaneUrl);
}

@Override 
public IdeaPageUrl clone() { 
    IdeaPageUrl other = new IdeaPageUrl();
    other.idea = this.idea.clone();
    other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
    return other;
}
}
