package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class CreateIdeaActionUrl extends Url {
public static String getName() { return "idea/docreate"; }
public com.bloatit.web.actions.CreateIdeaAction createPage() throws RedirectException{ 
    return new com.bloatit.web.actions.CreateIdeaAction(this); }
public CreateIdeaActionUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public CreateIdeaActionUrl(){
    super(getName());
    try {
        this.project.setValue(Loaders.fromStr(java.lang.String.class, "VLC"));
        this.category.setValue(Loaders.fromStr(java.lang.String.class, "Bug"));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<java.lang.String> description =     new UrlParameter<java.lang.String>("bloatit_idea_description", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> specification =     new UrlParameter<java.lang.String>("bloatit_idea_specification", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> project =     new UrlParameter<java.lang.String>("bloatit_idea_project", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> category =     new UrlParameter<java.lang.String>("bloatit_idea_category", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");
private UrlParameter<java.lang.String> lang =     new UrlParameter<java.lang.String>("bloatit_idea_lang", null, java.lang.String.class, Role.POST, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public java.lang.String getDescription(){ 
    return this.description.getValue();
}

public void setDescription(java.lang.String arg){ 
    this.description.setValue(arg);
}

public java.lang.String getSpecification(){ 
    return this.specification.getValue();
}

public void setSpecification(java.lang.String arg){ 
    this.specification.setValue(arg);
}

public java.lang.String getProject(){ 
    return this.project.getValue();
}

public void setProject(java.lang.String arg){ 
    this.project.setValue(arg);
}

public java.lang.String getCategory(){ 
    return this.category.getValue();
}

public void setCategory(java.lang.String arg){ 
    this.category.setValue(arg);
}

public java.lang.String getLang(){ 
    return this.lang.getValue();
}

public void setLang(java.lang.String arg){ 
    this.lang.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(description);
    register(specification);
    register(project);
    register(category);
    register(lang);
}

@Override 
public CreateIdeaActionUrl clone() { 
    CreateIdeaActionUrl other = new CreateIdeaActionUrl();
    other.description = this.description.clone();
    other.specification = this.specification.clone();
    other.project = this.project.clone();
    other.category = this.category.clone();
    other.lang = this.lang.clone();
    return other;
}
}
