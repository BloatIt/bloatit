package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class OfferActionUrl extends Url {
public OfferActionUrl() {
    super("OfferAction"); 
}
public OfferActionUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public OfferActionUrl clone() { 
    OfferActionUrl other = new OfferActionUrl();
    return other;
}
}
