package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class OfferListUrl extends UrlComponent {
public OfferListUrl() {
    super(); 
}
public OfferListUrl(Parameters params) {
    this();
    parseParameters(params);
}
private OfferComponentUrl offerComponentUrl = new OfferComponentUrl();

public OfferComponentUrl getOfferComponentUrl(){ 
    return this.offerComponentUrl;
}

public void setOfferComponentUrl(OfferComponentUrl arg0){ 
    this.offerComponentUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(offerComponentUrl);
}

public OfferListUrl clone() { 
    OfferListUrl other = new OfferListUrl();
    other.offerComponentUrl = this.offerComponentUrl.clone();
    return other;
}
}
