package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public final class MemberPageUrl extends Url {
public static String getName() { return "member"; }
public com.bloatit.web.html.pages.MemberPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.MemberPage(this); }
public MemberPageUrl(Parameters params, Parameters session) {
    this();
    parseParameters(params, false);
    parseParameters(session, true);
}
public MemberPageUrl(com.bloatit.framework.Member member) {
    this();
        this.member.setValue(member);
}
private MemberPageUrl(){
    super(getName());
}
private UrlParameter<com.bloatit.framework.Member> member =     new UrlParameter<com.bloatit.framework.Member>("id", null, com.bloatit.framework.Member.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\"");

public com.bloatit.framework.Member getMember(){ 
    return this.member.getValue();
}

public void setMember(com.bloatit.framework.Member arg){ 
    this.member.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(member);
}

@Override 
public MemberPageUrl clone() { 
    MemberPageUrl other = new MemberPageUrl();
    other.member = this.member.clone();
    return other;
}
}
