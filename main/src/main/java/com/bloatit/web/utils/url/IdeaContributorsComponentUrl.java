package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.url.Parameter;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;

@SuppressWarnings("unused")
public class IdeaContributorsComponentUrl extends UrlComponent {
public IdeaContributorsComponentUrl(Parameters params) {
    super();
    parseParameters(params);
}
public IdeaContributorsComponentUrl() {
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

public IdeaContributorsComponentUrl clone() { 
    IdeaContributorsComponentUrl other = new IdeaContributorsComponentUrl();
    other.participationsListUrl = this.participationsListUrl.clone();
    return other;
}
}
