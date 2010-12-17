package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class IndexPageUrl extends Url {
public static String getName() { return "IndexPage"; }
public com.bloatit.web.html.pages.IndexPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.IndexPage(this); }
public IndexPageUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public IndexPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public IndexPageUrl clone() { 
    IndexPageUrl other = new IndexPageUrl();
    return other;
}
}
