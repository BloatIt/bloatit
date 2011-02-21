package com.bloatit.framework.rest.resources;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.bloatit.framework.rest.RestResource;
import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.exception.RestException;
import com.bloatit.framework.webserver.components.rest.RestElement;
import com.bloatit.framework.webserver.masters.HttpResponse.StatusCode;

public class ErrorResource extends RestResource {
    private RestException exception;
    private String message;
    private StatusCode status;

    public ErrorResource(RequestMethod requestMethod, StatusCode status, String message) {
        super(requestMethod);
        this.message = message;
    }

    public ErrorResource(RequestMethod requestMethod, RestException exception) {
        super(requestMethod);
        this.exception = exception;
    }

    @Override
    protected void doGet() throws RestException {
        add(generateError());
    }

    @Override
    protected void doPost() throws RestException {
        add(generateError());
    }

    @Override
    protected void doPut() throws RestException {
        add(generateError());
    }

    @Override
    protected void doDelete() throws RestException {
        add(generateError());
    }

    private RestElement generateError() {
        RestElement error = new RestElement("error");

        if (message != null) {
            error.addAttribute("reason", status.toString());
            error.addText(message);
        } else {
            error.addAttribute("reason", exception.getStatus().toString());
            String result = "";
            result += exception.getMessage() + "\n";
            if (exception.getCause() != null) {
                result += exception.getCause();
            }
            result+="\n";
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            result += sw.toString();
            
            error.addText(result);
        }

        return error;
    }
}
