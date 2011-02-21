package com.bloatit.rest.resources;

import com.bloatit.framework.rest.RestResource;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.components.rest.RestElement;

public class TestResource extends RestResource {
    public TestResource(RequestMethod requestMethod) {
        super(requestMethod);
    }

    protected void doGet() {
        add(new RestElement("plop"));
    }

    @Override
    protected void doPost() throws RestException {
        super.doPost();
    }

}
