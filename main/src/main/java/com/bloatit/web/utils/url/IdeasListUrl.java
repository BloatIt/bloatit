package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class IdeasListUrl extends Url {
public static String getName() { return "IdeasList"; }
public IdeasListUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public IdeasListUrl() {
    super(getName());
}
private HtmlPagedListUrl pagedIdeaListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getPagedIdeaListUrl(){ 
    return this.pagedIdeaListUrl;
}

public void setPagedIdeaListUrl(HtmlPagedListUrl arg0){ 
    this.pagedIdeaListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(pagedIdeaListUrl);
}

public IdeasListUrl clone() { 
    IdeasListUrl other = new IdeasListUrl();
    other.pagedIdeaListUrl = this.pagedIdeaListUrl.clone();
    return other;
}
}
