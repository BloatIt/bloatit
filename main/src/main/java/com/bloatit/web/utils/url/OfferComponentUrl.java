package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class OfferComponentUrl extends UrlComponent {
public OfferComponentUrl() {
    super(); 
}
public OfferComponentUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public OfferComponentUrl clone() { 
    OfferComponentUrl other = new OfferComponentUrl();
    return other;
}
}
