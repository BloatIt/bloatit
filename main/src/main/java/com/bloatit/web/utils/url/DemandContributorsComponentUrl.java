package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class DemandContributorsComponentUrl extends UrlComponent {
public DemandContributorsComponentUrl() {
    super(); 
}
public DemandContributorsComponentUrl(Parameters params) {
    this();
    parseParameters(params);
}
private PagedListUrl participationsListUrl = new PagedListUrl();

public PagedListUrl getParticipationsListUrl(){ 
    return this.participationsListUrl;
}

public void setParticipationsListUrl(PagedListUrl arg0){ 
    this.participationsListUrl = arg0;
}


@Override 
protected void doRegister() { 
    register(participationsListUrl);
}

public DemandContributorsComponentUrl clone() { 
    DemandContributorsComponentUrl other = new DemandContributorsComponentUrl();
    other.participationsListUrl = this.participationsListUrl.clone();
    return other;
}
}
