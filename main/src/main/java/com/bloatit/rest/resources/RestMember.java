package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.Member;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
