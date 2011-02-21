package com.bloatit.rest;

import java.util.HashSet;
import java.util.Set;

import com.bloatit.common.Log;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;

public class BloatitRestServer extends RestServer {
    RequestMethod requestMethod;
    
    @Override
    protected RestResource constructRestResource(String pageCode, RequestMethod requestMethod, Parameters params, Session session) {
        this.requestMethod = requestMethod;
        
        if(pageCode.equals("rest/plop")){
            Log.rest().trace("Found resource rest/plop");
            return new TestResource(requestMethod);
        }
        
        return null;
    }
    
    @Override
    protected RestResource generateErrorResource(StatusCode statusCode) {
        return new ResourceNotFound(requestMethod);
    }

    @Override
    protected Set<String> getResourcesDirectories() {
        HashSet<String> directories = new HashSet<String>();
        directories.add("rest");
        return directories;
    }
}
