package com.bloatit.web.utils.url;

import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class TestPageUrl extends Url {
public static String getName() { return "test"; }
public com.bloatit.web.html.pages.TestPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.TestPage(this); }
public TestPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public TestPageUrl(){
    super(getName());
}


@Override 
protected void doRegister() { 
}

@Override 
public TestPageUrl clone() { 
    TestPageUrl other = new TestPageUrl();
    return other;
}
}
