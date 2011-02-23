package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.HighlightDemand;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestHighlightDemandList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestHighlightDemand extends RestElement<HighlightDemand>{ 
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
    public static RestHighlightDemandList getAll(){
        //TODO auto generated code
        return null;
    }

    HighlightDemand getModel(){
        return model;
    }
}
