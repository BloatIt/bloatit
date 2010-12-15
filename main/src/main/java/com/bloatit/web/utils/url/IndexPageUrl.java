package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IndexPageUrl extends Url {
public IndexPageUrl() {
    super("IndexPage"); 
}
public IndexPageUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public IndexPageUrl clone() { 
    IndexPageUrl other = new IndexPageUrl();
    return other;
}
}
