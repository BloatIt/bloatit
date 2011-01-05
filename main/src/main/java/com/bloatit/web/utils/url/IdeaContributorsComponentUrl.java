package com.bloatit.web.utils.url;


@SuppressWarnings("unused")
public final class IdeaContributorsComponentUrl extends UrlComponent {
public IdeaContributorsComponentUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public IdeaContributorsComponentUrl(){
    super();
}
private HtmlPagedListUrl participationsListUrl = new HtmlPagedListUrl();

public HtmlPagedListUrl getParticipationsListUrl(){ 
    return this.participationsListUrl;
}

public void setParticipationsListUrl(HtmlPagedListUrl arg){ 
    this.participationsListUrl = arg;
}


@Override 
protected void doRegister() { 
    register(participationsListUrl);
}

@Override 
public IdeaContributorsComponentUrl clone() { 
    IdeaContributorsComponentUrl other = new IdeaContributorsComponentUrl();
    other.participationsListUrl = this.participationsListUrl.clone();
    return other;
}
}
