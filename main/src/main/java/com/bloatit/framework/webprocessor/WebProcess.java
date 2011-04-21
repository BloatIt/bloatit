package com.bloatit.framework.webprocessor;

import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;

public abstract class WebProcess extends Action {

    private final String processId;

    private WebProcess father;

    public WebProcess(final Url url) {
        super(url);
        processId = Context.getSession().createWebProcess(this);
    }

    public String getId() {
        return processId;
    }

    public void addChildProcess(final WebProcess child) {
        child.father = this;
        child.notifyChildAdded(child);
    }

    public WebProcess getFather() {
        return father;
    }

    public void load() {
        if (father != null) {
            father.load();
        }
        doLoad();
    }

    /**
     * Call after session extraction. Used to reload database objects TODO:
     * verify if this is thread safe
     */
    protected abstract void doLoad();

    public Url close() {
        Context.getSession().destroyWebProcess(this);
        if (father != null) {
            return father.notifyChildClosed(this);
        }
        return null;
    }

    protected void notifyChildAdded(final WebProcess subProcess) {
        // Implement me in subclass if you wish.
    }

    protected Url notifyChildClosed(final WebProcess subProcess) {
        // Implement me in subclass if you wish.
        return null;
    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        // Nothing to do
    }
}
