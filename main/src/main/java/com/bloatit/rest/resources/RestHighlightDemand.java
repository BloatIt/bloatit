package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.HighlightDemand;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

@XmlRootElement
public class RestHighlightDemand extends RestElement{ 
    private HighlightDemand model;

    protected RestHighlightDemand(HighlightDemand model){
        this.model=model;
    }

    @REST(name = "highlightdemands", method = RequestMethod.GET)
    public static RestHighlightDemand getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "highlightdemands", method = RequestMethod.GET)
    public static RestList<RestHighlightDemand> getAll(){
        //TODO auto generated code
        return null;
    }

    HighlightDemand getModel(){
        return model;
    }
}
