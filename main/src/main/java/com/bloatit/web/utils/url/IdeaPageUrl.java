package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class IdeaPageUrl extends Url {
public static String getName() { return "IdeaPage"; }
public com.bloatit.web.html.pages.idea.IdeaPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.idea.IdeaPage(this); }
public IdeaPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public IdeaPageUrl(com.bloatit.framework.Demand idea) {
    super(getName());
        this.idea = idea;
}
private IdeaPageUrl(){
    super(getName());
}
private com.bloatit.framework.Demand idea;
private IdeaTabPaneUrl demandTabPaneUrl = new IdeaTabPaneUrl();

public com.bloatit.framework.Demand getIdea(){ 
    return this.idea;
}

public void setIdea(com.bloatit.framework.Demand arg0){ 
    this.idea = arg0;
}

public java.lang.String getTitle(){ 
    if (idea != null) {
        return idea.getTitle();
    } else {
        return null;
    }
}

public IdeaTabPaneUrl getDemandTabPaneUrl(){ 
    return this.demandTabPaneUrl;
}

public void setDemandTabPaneUrl(IdeaTabPaneUrl arg0){ 
    this.demandTabPaneUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("id", getIdea(), com.bloatit.framework.Demand.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(new Parameter("title", getTitle(), java.lang.String.class, Role.PRETTY, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
    register(demandTabPaneUrl);
}

public IdeaPageUrl clone() { 
    IdeaPageUrl other = new IdeaPageUrl();
    other.idea = this.idea;
    other.demandTabPaneUrl = this.demandTabPaneUrl.clone();
    return other;
}
}
