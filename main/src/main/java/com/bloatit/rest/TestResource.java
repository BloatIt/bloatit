package com.bloatit.rest;

import com.bloatit.framework.webserver.components.rest.RestElement;
import com.bloatit.rest.RestServer.RequestMethod;

public class TestResource extends RestResource {
    public TestResource(RequestMethod requestMethod) {
        super(requestMethod);
    }

    protected void doGet(){
        add(new RestElement("plop"));
    }
    
    @Override
    protected void doPost() throws RestException {
        super.doPost();
    }
    
}
