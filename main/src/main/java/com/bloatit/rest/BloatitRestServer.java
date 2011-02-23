package com.bloatit.rest;

import java.util.HashSet;
import java.util.Set;

import com.bloatit.common.Log;
import com.bloatit.framework.rest.RestResource;
import com.bloatit.framework.rest.RestServer;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.rest.resources.ErrorResource;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;
import com.bloatit.rest.resources.TestResource;

public class BloatitRestServer extends RestServer {
    RequestMethod requestMethod;

    @Override
    protected RestResource constructRestResource(String pageCode, RequestMethod requestMethod, Parameters params, Session session) {
        this.requestMethod = requestMethod;

        if (pageCode.equals("plop")) {
            Log.rest().trace("Found resource plop");
            return new TestResource(requestMethod);
        }

        return null;
    }

    @Override
    protected Set<String> getResourcesDirectories() {
        HashSet<String> directories = new HashSet<String>();
        directories.add("rest");
        return directories;
    }

    @Override
    protected RestResource generateErrorResource(StatusCode status, String message) {
        return new ErrorResource(requestMethod, status, message);
    }

    @Override
    protected RestResource generateErrorResource(RestException exception) {
        return new ErrorResource(requestMethod, exception);
    }

    @Override
    public boolean initialize() {
        return true;
    }
}
