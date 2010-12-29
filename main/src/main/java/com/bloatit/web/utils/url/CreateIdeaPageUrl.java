package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class CreateIdeaPageUrl extends Url {
public static String getName() { return "idea/create"; }
public com.bloatit.web.html.pages.CreateIdeaPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.CreateIdeaPage(this); }
public CreateIdeaPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public CreateIdeaPageUrl(){
    super(getName());
    try {
        this.description.setValue(Loaders.fromStr(java.lang.String.class, ""));
    } catch (ConversionErrorException e) {
        e.printStackTrace();
        assert false ;
    }
}
private UrlParameter<java.lang.String> description =     new UrlParameter<java.lang.String>("bloatit_idea_description", null, java.lang.String.class, Role.SESSION, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public java.lang.String getDescription(){ 
    return this.description.getValue();
}

public void setDescription(java.lang.String arg){ 
    this.description.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(description);
}

public CreateIdeaPageUrl clone() { 
    CreateIdeaPageUrl other = new CreateIdeaPageUrl();
    other.description = this.description.clone();
    return other;
}
}
