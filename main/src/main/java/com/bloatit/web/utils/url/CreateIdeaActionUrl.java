package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class CreateIdeaActionUrl extends Url {
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
}


@Override 
protected void doRegister() { 
}

public CreateIdeaActionUrl clone() { 
    CreateIdeaActionUrl other = new CreateIdeaActionUrl();
    return other;
}
}
