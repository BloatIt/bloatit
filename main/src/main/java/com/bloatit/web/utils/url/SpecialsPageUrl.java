package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class SpecialsPageUrl extends Url {
public static String getName() { return "SpecialsPage"; }
public SpecialsPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public SpecialsPageUrl() {
    super(getName());
}


@Override 
protected void doRegister() { 
}

public SpecialsPageUrl clone() { 
    SpecialsPageUrl other = new SpecialsPageUrl();
    return other;
}
}
