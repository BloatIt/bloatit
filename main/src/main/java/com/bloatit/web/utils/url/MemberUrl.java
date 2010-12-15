package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.RequestParam.Role;

public class MemberUrl extends Url {
public MemberUrl() {
    super("member"); 
}
public MemberUrl(Parameters params) {
    this();
    parseParameters(params);
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

public MemberUrl clone() { 
    MemberUrl other = new MemberUrl();
    other.member = this.member;
    return other;
}
}
