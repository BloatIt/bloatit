//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.actions;

import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;

public abstract class WebProcess extends ElveosAction {

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
    protected Url checkRightsAndEverything(ElveosUserToken token) {
        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        // Nothing to do
    }
}
