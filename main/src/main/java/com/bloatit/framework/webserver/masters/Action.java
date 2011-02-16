/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.framework.webserver.masters;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.ModelManagerAccessor;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.framework.webserver.url.Url;

/**
 * The mother of all actions
 */
public abstract class Action implements Linkable {

    protected final Session session;
    private final Url actionUrl;

    public Action(final Url url) {
        ModelManagerAccessor.open();
        this.actionUrl = url;
        session = Context.getSession();
    }

    @Override
    public final void writeToHttp(final HttpResponse response) throws RedirectException, IOException {
        Log.framework().trace("Processing action: " + actionUrl.urlString());
        Url url = process();
        if (url != null) {
            response.writeRedirect(url.urlString());
        } else {
            response.writeRedirect(new PageNotFoundUrl().urlString());
        }
    }

    public final Url process() throws RedirectException {
        if (actionUrl.getMessages().hasMessage(Level.ERROR)) {
            return doProcessErrors();
        }
        return doProcess();
    }

    protected abstract Url doProcess() throws RedirectException;

    protected abstract Url doProcessErrors() throws RedirectException;

}
