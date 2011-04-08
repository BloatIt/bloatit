/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.Session;
import com.bloatit.framework.webprocessor.WebServer;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;

/**
 * The mother of all actions
 */
public abstract class Action implements Linkable {

    protected final Session session;
    private final Url actionUrl;

    public Action(final Url url) {
        this.actionUrl = url;
        session = Context.getSession();
    }

    @Override
    public final void writeToHttp(final HttpResponse response, WebServer server) throws RedirectException, IOException {
        Log.framework().trace("Processing action: " + actionUrl.urlString());
        final Url url = process();
        if (url != null) {
            if(url.isAction()) {
                final Parameters parameters = url.getStringParameters();

                final Linkable linkable = server.constructLinkable(url.getCode(), parameters, session);
                linkable.writeToHttp(response, server);
            } else {
                response.writeRedirect(url.urlString());
            }

        } else {
            response.writeRedirect(new PageNotFoundUrl().urlString());
        }
    }

    public final Url process() {
        if (!actionUrl.getMessages().isEmpty()) {
            return doProcessErrors();
        }
        return doProcess();
    }

    protected abstract Url doProcess();

    protected abstract Url doProcessErrors();

}
