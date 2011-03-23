package com.bloatit.framework.webserver;

import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;

public abstract class WebProcess extends Action{

    private final String processId;

    public WebProcess(Url url) {
        super(url);
        processId = Context.getSession().createWebProcess(this);

    }

    public String getId() {
        return processId;
    }

    /**
     * Call after session extraction.
     * Used to reload database objects
     * TODO: verify if this is thread safe
     */
    public abstract void load();


    public void close() {
        Context.getSession().destroyWebProcess(this);
    }



}
