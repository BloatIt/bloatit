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
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.xcgiserver.HttpResponse;

/**
 * The mother of all actions
 */
public abstract class Action implements Linkable {

    protected static final Url NO_ERROR = null;

    protected final Session session;
    private final Url actionUrl;

    public Action(final Url url) {
        this.actionUrl = url;
        session = Context.getSession();
    }

    @Override
    public final void writeToHttp(final HttpResponse response, final WebProcessor server) throws RedirectException, IOException {
        Log.framework().trace("Processing action: " + actionUrl.urlString());
        final Url url = process();
        if (url != null) {
            if (url.isAction()) {
                Log.framework().info("Execute chained action: "+url.urlString());
                final Parameters parameters = url.getStringParameters();

                final Linkable linkable = server.constructLinkable(url.getCode(), parameters, session);
                linkable.writeToHttp(response, server);
            } else {
                response.writeRedirect(url.urlString());
            }

        } else {
            Log.framework().error("Null destination url, redirect to page not found");
            response.writeRedirect(new PageNotFoundUrl().urlString());
        }
    }

    private final Url process() {
        if (actionUrl.hasError()) {
            session.notifyList(actionUrl.getMessages());
            transmitParameters();
            return doProcessErrors();
        }
        final Url checkParameters = checkRightsAndEverything();
        if (checkParameters != NO_ERROR) {
            transmitParameters();
            return checkParameters;
        }
        return doProcess();
    }

    /**
     * <p>
     * The url system perform some checks on constraints. You may want to add
     * more specific constraint checking by overriding this method.
     * </p>
     *
     * @return null if there is no error, the url where you want to be
     *         redirected otherwise.
     */
    protected abstract Url checkRightsAndEverything();

    /**
     * <p>
     * Called when there is no errors (See {@link Url#getMessages()} and
     * {@link #checkRightsAndEverything()}).
     * </p>
     * <p>
     * This is the normal case. The {@link #transmitParameters()} method is not
     * called whene there is no error. So if your action fail during the
     * doProcess(), you may want to call the {@link #transmitParameters()}
     * method by yourself.
     * </p>
     *
     * @return the redirect url of this action.
     */
    protected abstract Url doProcess();

    /**
     * Called when there is at least one error (See {@link Url#getMessages()}
     * and {@link #checkRightsAndEverything()}).
     *
     * @return the redirect url of this action.
     */
    protected abstract Url doProcessErrors();

    /**
     * Override and save all your parameters into session
     */
    protected abstract void transmitParameters();

}
