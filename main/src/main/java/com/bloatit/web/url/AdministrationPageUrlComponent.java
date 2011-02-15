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
public final class AdministrationPageUrlComponent extends UrlComponent {
public AdministrationPageUrlComponent(Parameters params, SessionParameters session) {
    this();
    parseSessionParameters(session);
    parseParameters(params);
}
public AdministrationPageUrlComponent(){
    super();
    try {
        this.orderByMemberAsc.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.orderByAsGroupAsc.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.orderByMemberDesc.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.orderByAsGroupDesc.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.deletedOnly.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.nonDeletedOnly.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.withoutFile.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.withFile.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.withAnyGroup.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
        this.withNoGroup.setValue(Loaders.fromStr(java.lang.Boolean.class, "false"));
    } catch (ConversionErrorException e) {
        Log.web().fatal(e);
        assert false ;
    }
}
private UrlParameter<java.lang.Boolean, java.lang.Boolean> orderByMemberAsc =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("orderByMemberAsc", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> orderByAsGroupAsc =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("orderByAsGroupAsc", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> orderByMemberDesc =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("orderByMemberDesc", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> orderByAsGroupDesc =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("orderByAsGroupDesc", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> deletedOnly =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("deletedOnly", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> nonDeletedOnly =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("nonDeletedOnly", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> withoutFile =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("withoutFile", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> withFile =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("withFile", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> withAnyGroup =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("withAnyGroup", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());
private UrlParameter<java.lang.Boolean, java.lang.Boolean> withNoGroup =  new UrlParameter<java.lang.Boolean, java.lang.Boolean>(null, new UrlParameterDescription<java.lang.Boolean>("withNoGroup", java.lang.Boolean.class, Role.POST, "false", RequestParam.DEFAULT_ERROR_MSG, Level.ERROR), new UrlParameterConstraints<java.lang.Boolean>());

public java.lang.Boolean getOrderByMemberAsc(){ 
    return this.orderByMemberAsc.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getOrderByMemberAscParameter(){ 
    return this.orderByMemberAsc;
}

public void setOrderByMemberAsc(java.lang.Boolean arg){ 
    this.orderByMemberAsc.setValue(arg);
}

public java.lang.Boolean getOrderByAsGroupAsc(){ 
    return this.orderByAsGroupAsc.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getOrderByAsGroupAscParameter(){ 
    return this.orderByAsGroupAsc;
}

public void setOrderByAsGroupAsc(java.lang.Boolean arg){ 
    this.orderByAsGroupAsc.setValue(arg);
}

public java.lang.Boolean getOrderByMemberDesc(){ 
    return this.orderByMemberDesc.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getOrderByMemberDescParameter(){ 
    return this.orderByMemberDesc;
}

public void setOrderByMemberDesc(java.lang.Boolean arg){ 
    this.orderByMemberDesc.setValue(arg);
}

public java.lang.Boolean getOrderByAsGroupDesc(){ 
    return this.orderByAsGroupDesc.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getOrderByAsGroupDescParameter(){ 
    return this.orderByAsGroupDesc;
}

public void setOrderByAsGroupDesc(java.lang.Boolean arg){ 
    this.orderByAsGroupDesc.setValue(arg);
}

public java.lang.Boolean getDeletedOnly(){ 
    return this.deletedOnly.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getDeletedOnlyParameter(){ 
    return this.deletedOnly;
}

public void setDeletedOnly(java.lang.Boolean arg){ 
    this.deletedOnly.setValue(arg);
}

public java.lang.Boolean getNonDeletedOnly(){ 
    return this.nonDeletedOnly.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getNonDeletedOnlyParameter(){ 
    return this.nonDeletedOnly;
}

public void setNonDeletedOnly(java.lang.Boolean arg){ 
    this.nonDeletedOnly.setValue(arg);
}

public java.lang.Boolean getWithoutFile(){ 
    return this.withoutFile.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getWithoutFileParameter(){ 
    return this.withoutFile;
}

public void setWithoutFile(java.lang.Boolean arg){ 
    this.withoutFile.setValue(arg);
}

public java.lang.Boolean getWithFile(){ 
    return this.withFile.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getWithFileParameter(){ 
    return this.withFile;
}

public void setWithFile(java.lang.Boolean arg){ 
    this.withFile.setValue(arg);
}

public java.lang.Boolean getWithAnyGroup(){ 
    return this.withAnyGroup.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getWithAnyGroupParameter(){ 
    return this.withAnyGroup;
}

public void setWithAnyGroup(java.lang.Boolean arg){ 
    this.withAnyGroup.setValue(arg);
}

public java.lang.Boolean getWithNoGroup(){ 
    return this.withNoGroup.getValue();
}

public UrlParameter<java.lang.Boolean, java.lang.Boolean> getWithNoGroupParameter(){ 
    return this.withNoGroup;
}

public void setWithNoGroup(java.lang.Boolean arg){ 
    this.withNoGroup.setValue(arg);
}


@Override 
protected void doRegister() { 
    register(orderByMemberAsc);
    register(orderByAsGroupAsc);
    register(orderByMemberDesc);
    register(orderByAsGroupDesc);
    register(deletedOnly);
    register(nonDeletedOnly);
    register(withoutFile);
    register(withFile);
    register(withAnyGroup);
    register(withNoGroup);
}

@Override 
public AdministrationPageUrlComponent clone() { 
    AdministrationPageUrlComponent other = new AdministrationPageUrlComponent();
    other.orderByMemberAsc = this.orderByMemberAsc.clone();
    other.orderByAsGroupAsc = this.orderByAsGroupAsc.clone();
    other.orderByMemberDesc = this.orderByMemberDesc.clone();
    other.orderByAsGroupDesc = this.orderByAsGroupDesc.clone();
    other.deletedOnly = this.deletedOnly.clone();
    other.nonDeletedOnly = this.nonDeletedOnly.clone();
    other.withoutFile = this.withoutFile.clone();
    other.withFile = this.withFile.clone();
    other.withAnyGroup = this.withAnyGroup.clone();
    other.withNoGroup = this.withNoGroup.clone();
    return other;
}
}
