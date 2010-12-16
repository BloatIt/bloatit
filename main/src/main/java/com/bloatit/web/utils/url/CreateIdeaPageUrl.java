package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class CreateIdeaPageUrl extends Url {
public static String getName() { return "CreateIdeaPage"; }
public com.bloatit.web.html.pages.CreateIdeaPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.CreateIdeaPage(this); }
public CreateIdeaPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public CreateIdeaPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public CreateIdeaPageUrl clone() { 
    CreateIdeaPageUrl other = new CreateIdeaPageUrl();
    return other;
}
}
