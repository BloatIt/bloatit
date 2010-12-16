package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class IndexPageUrl extends Url {
public static String getName() { return "IndexPage"; }
public IndexPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
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
