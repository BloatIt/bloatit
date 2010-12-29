package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.UrlParameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.Loaders.*;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class SpecialsPageUrl extends Url {
public static String getName() { return "special"; }
public com.bloatit.web.html.pages.SpecialsPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.SpecialsPage(this); }
public SpecialsPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public SpecialsPageUrl(){
    super(getName());
}


@Override 
protected void doRegister() { 
}

@Override 
public SpecialsPageUrl clone() { 
    SpecialsPageUrl other = new SpecialsPageUrl();
    return other;
}
}
