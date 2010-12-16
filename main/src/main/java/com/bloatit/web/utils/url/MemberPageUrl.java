package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.utils.annotations.Loaders;
import com.bloatit.web.utils.annotations.RequestParamSetter.ConversionErrorException;
import com.bloatit.web.exceptions.RedirectException;

@SuppressWarnings("unused")
public class MemberPageUrl extends Url {
public static String getName() { return "MemberPage"; }
public com.bloatit.web.html.pages.MemberPage createPage() throws RedirectException{ 
    return new com.bloatit.web.html.pages.MemberPage(this); }
public MemberPageUrl(Parameters params) {
    super(getName());
    parseParameters(params);
}
public MemberPageUrl(com.bloatit.framework.Member member) {
    super(getName());
        this.member = member;
}
private MemberPageUrl(){
    super(getName());
}
private com.bloatit.framework.Member member;

public com.bloatit.framework.Member getMember(){ 
    return this.member;
}

public void setMember(com.bloatit.framework.Member arg0){ 
    this.member = arg0;
}


@Override 
protected void doRegister() { 
    register(new Parameter("id", getMember(), com.bloatit.framework.Member.class, Role.GET, Level.ERROR, "Error: invalid value (%value) for parameter \"%param\""));
}

public MemberPageUrl clone() { 
    MemberPageUrl other = new MemberPageUrl();
    other.member = this.member;
    return other;
}
}
