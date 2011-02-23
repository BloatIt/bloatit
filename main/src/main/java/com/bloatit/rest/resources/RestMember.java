package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Member;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestMember extends RestElement{ 
    private Member model;

    protected RestMember(Member model){
        this.model=model;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static RestMember getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static RestList<RestMember> getAll(){
        //TODO auto generated code
        return null;
    }

    Member getModel(){
        return model;
    }
}
