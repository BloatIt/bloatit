package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.TeamRole;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestTeamRole extends RestElement{ 
    private TeamRole model;

    protected RestTeamRole(TeamRole model){
        this.model=model;
    }

    @REST(name = "teamroles", method = RequestMethod.GET)
    public static RestTeamRole getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "teamroles", method = RequestMethod.GET)
    public static RestList<RestTeamRole> getAll(){
        //TODO auto generated code
        return null;
    }

    TeamRole getModel(){
        return model;
    }
}
