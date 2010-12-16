package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class TestPageUrl extends Url {
public static String getName() { return "TestPage"; }
public TestPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
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
