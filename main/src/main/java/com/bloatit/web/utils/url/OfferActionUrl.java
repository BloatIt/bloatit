package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class OfferActionUrl extends Url {
public static String getName() { return "OfferAction"; }
public OfferActionUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public OfferActionUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public OfferActionUrl clone() { 
    OfferActionUrl other = new OfferActionUrl();
    return other;
}
}
