package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.JoinGroupInvitation;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestJoinGroupInvitation extends RestElement{ 
    private JoinGroupInvitation model;

    protected RestJoinGroupInvitation(JoinGroupInvitation model){
        this.model=model;
    }

    @REST(name = "joingroupinvitations", method = RequestMethod.GET)
    public static RestJoinGroupInvitation getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "joingroupinvitations", method = RequestMethod.GET)
    public static RestList<RestJoinGroupInvitation> getAll(){
        //TODO auto generated code
        return null;
    }

    JoinGroupInvitation getModel(){
        return model;
    }
}
