package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Contribution;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestContributionList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestContribution extends RestElement<Contribution>{ 
    private Contribution model;

    protected RestContribution(Contribution model){
        this.model=model;
    }

    @REST(name = "contributions", method = RequestMethod.GET)
    public static RestContribution getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "contributions", method = RequestMethod.GET)
    public static RestContributionList getAll(){
        //TODO auto generated code
        return null;
    }

    Contribution getModel(){
        return model;
    }
}
