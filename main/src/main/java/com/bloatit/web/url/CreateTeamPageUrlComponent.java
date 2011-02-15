package com.bloatit.web.url;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.*;
import com.bloatit.framework.webserver.url.*;
import com.bloatit.framework.webserver.url.Loaders.*;

@SuppressWarnings("unused")
public final class CreateTeamPageUrlComponent extends UrlComponent {
public CreateTeamPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public CreateTeamPageUrlComponent(){
    super();
}


@Override 
protected void doRegister() { 
}

@Override 
public CreateTeamPageUrlComponent clone() { 
    CreateTeamPageUrlComponent other = new CreateTeamPageUrlComponent();
    return other;
}
}
