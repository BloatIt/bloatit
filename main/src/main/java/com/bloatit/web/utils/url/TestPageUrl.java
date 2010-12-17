package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class TestPageUrl extends Url {
public static String getName() { return "TestPage"; }
public com.bloatit.web.html.pages.TestPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.TestPage(this); }
public TestPageUrl(Parameters params, Parameters session) {
    super(getName());
    parseParameters(params, false);
    parseParameters(session, true);
}
public TestPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public TestPageUrl clone() { 
    TestPageUrl other = new TestPageUrl();
    return other;
}
}
