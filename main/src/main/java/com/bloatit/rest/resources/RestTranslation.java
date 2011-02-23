package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.Translation;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.list.RestTranslationList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestTranslation extends RestElement<Translation>{ 
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
    public static RestTranslationList getAll(){
        //TODO auto generated code
        return null;
    }

    Translation getModel(){
        return model;
    }
}
