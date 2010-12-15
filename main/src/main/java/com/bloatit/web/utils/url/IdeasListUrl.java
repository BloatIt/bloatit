package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IdeasListUrl extends Url {
public IdeasListUrl() {
    super("IdeasList"); 
}
public IdeasListUrl(Parameters params) {
    this();
    parseParameters(params);
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
