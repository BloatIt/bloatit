package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.HighlightDemand;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

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
