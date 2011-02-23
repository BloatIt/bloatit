package com.bloatit.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;
import com.bloatit.model.CacheManager;
import com.bloatit.rest.RestElement;
import com.bloatit.rest.utils.RestList;

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
