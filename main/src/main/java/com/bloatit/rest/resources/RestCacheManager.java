package com.bloatit.rest.resources;

import com.bloatit.rest.RestElement;
import com.bloatit.model.CacheManager;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.bloatit.rest.utils.RestList;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class RestCacheManager extends RestElement{ 
    private CacheManager model;

    protected RestCacheManager(CacheManager model){
        this.model=model;
    }

    @REST(name = "cachemanagers", method = RequestMethod.GET)
    public static RestCacheManager getById(int id){
        //TODO auto generated code
        return null;
    }

    @REST(name = "cachemanagers", method = RequestMethod.GET)
    public static RestList<RestCacheManager> getAll(){
        //TODO auto generated code
        return null;
    }

    CacheManager getModel(){
        return model;
    }
}
