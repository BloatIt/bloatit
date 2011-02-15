package com.bloatit.web.url;

import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ConversionErrorException;
import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.*;
import com.bloatit.framework.webserver.url.*;
import com.bloatit.framework.webserver.url.Loaders.*;

@SuppressWarnings("unused")
public final class MemberPageUrlComponent extends UrlComponent {
public MemberPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public MemberPageUrlComponent(com.bloatit.model.Member member) {
    this();
        this.member.setValue(member);
}
private MemberPageUrlComponent(){
    super();
}
private UrlParameter<com.bloatit.model.Member, com.bloatit.model.Member> member =  new UrlParameter<com.bloatit.model.Member, com.bloatit.model.Member>(null, new UrlParameterDescription<com.bloatit.model.Member>("id", com.bloatit.model.Member.class, Role.GET, RequestParam.DEFAULT_DEFAULT_VALUE, RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<com.bloatit.model.Member>(-2147483648, false, 2147483647, false, false, 2147483647, 2147483647, ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG, "The id of the member is incorrect or missing", ParamConstraint.DEFAULT_ERROR_MSG, ParamConstraint.DEFAULT_ERROR_MSG));

public com.bloatit.model.Member getMember(){ 
    return this.member.getValue();
}

public UrlParameter<com.bloatit.model.Member, com.bloatit.model.Member> getMemberParameter(){ 
    return this.member;
}

public void setMember(com.bloatit.model.Member arg){ 
    this.member.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(member);
}

@Override 
public MemberPageUrlComponent clone() { 
    MemberPageUrlComponent other = new MemberPageUrlComponent();
    other.member = this.member.clone();
    return other;
}
}
