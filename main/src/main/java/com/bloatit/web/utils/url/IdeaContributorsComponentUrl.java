package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class IdeaContributorsComponentUrl extends UrlComponent {
public IdeaContributorsComponentUrl() {
    super(); 
}
public IdeaContributorsComponentUrl(Parameters params) {
    this();
    parseParameters(params);
}
private HtmlPagedListUrl participationsListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getParticipationsListUrl(){ 
    return this.participationsListUrl;
}

public void setParticipationsListUrl(HtmlPagedListUrl arg0){ 
    this.participationsListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(participationsListUrl);
}

public IdeaContributorsComponentUrl clone() { 
    IdeaContributorsComponentUrl other = new IdeaContributorsComponentUrl();
    other.participationsListUrl = this.participationsListUrl.clone();
    return other;
}
}
