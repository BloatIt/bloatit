package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Translation;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestTranslation extends RestElement{ 
    private Translation model;

    protected RestTranslation(Translation model){
        this.model=model;
    }

    @REST(name = "translations", method = RequestMethod.GET)
    public static RestTranslation getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "translations", method = RequestMethod.GET)
    public static RestList<RestTranslation> getAll(){
        //TODO auto generated code
        return null;
    }

    Translation getModel(){
        return model;
    }
}
