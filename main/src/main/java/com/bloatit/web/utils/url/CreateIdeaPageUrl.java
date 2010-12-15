package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class CreateIdeaPageUrl extends Url {
public CreateIdeaPageUrl() {
    super("CreateIdeaPage"); 
}
public CreateIdeaPageUrl(Parameters params) {
    this();
    parseParameters(params);
}


@Override 
protected void doRegister() { 
}

public CreateIdeaPageUrl clone() { 
    CreateIdeaPageUrl other = new CreateIdeaPageUrl();
    return other;
}
}
