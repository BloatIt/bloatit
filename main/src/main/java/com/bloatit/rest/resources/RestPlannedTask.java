package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.PlannedTask;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestPlannedTask extends RestElement{ 
    private PlannedTask model;

    protected RestPlannedTask(PlannedTask model){
        this.model=model;
    }

    @REST(name = "plannedtasks", method = RequestMethod.GET)
    public static RestPlannedTask getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "plannedtasks", method = RequestMethod.GET)
    public static RestList<RestPlannedTask> getAll(){
        //TODO auto generated code
        return null;
    }

    PlannedTask getModel(){
        return model;
    }
}
