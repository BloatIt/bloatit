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
package com.bloatit.framework.webserver;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.scgiserver.HttpHeader;
import com.bloatit.framework.scgiserver.HttpPost;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.webserver.masters.Linkable;
import com.bloatit.framework.webserver.masters.PageNotFound;
import com.bloatit.framework.webserver.url.Url;

public final class DispatchServer {
    private final Map<String, Class<? extends Url>> urls = new HashMap<String, Class<? extends Url>>();

    public DispatchServer() {
        // Nothing here.
    }

    public void addLinkable(final String name, final Class<? extends Url> urlClass) {
        urls.put(name, urlClass);
    }

    public void process(final HttpHeader header, final HttpPost post, final HttpResponse response) throws IOException {
        final Session session = findSession(header);

        try {
            com.bloatit.model.Framework.lock();

            Context.reInitializeContext(header, session);

            final String pageCode = header.getQueryString().getPageName();

            // Merge post and get parameters.
            final Parameters parameters = new Parameters();
            parameters.putAll(header.getQueryString().getParameters());
            parameters.putAll(header.getQueryString().getGetParameters());
            parameters.putAll(post.getParameters());

            try {
                com.bloatit.data.SessionManager.beginWorkUnit();
                final Linkable linkable = constructLinkable(pageCode, parameters, session);
                linkable.writeToHttp(response);
            } catch (final RedirectException e) {
                Log.framework().info("Redirect to " + e.getUrl(), e);
                response.writeRedirect(e.getUrl().urlString());
            } finally {
                com.bloatit.data.SessionManager.endWorkUnitAndFlush();
            }

        } catch (final InterruptedException ex) {
            Log.framework().fatal("Cannot lock the framework.", ex);
        } finally {
            try {
                com.bloatit.model.Framework.unLock();
            } catch (final Exception e) {
                Log.framework().fatal("Cannot unlock the framework.", e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    // It's OK
    private Linkable constructLinkable(final String pageCode, final Parameters params, final Session session) {
        try {
            final Class<? extends Url> urlClass = urls.get(pageCode);
            if (urlClass != null) {
                return urlClass.getConstructor(Parameters.class, Parameters.class).newInstance(params, session.getParams()).createPage();
            }
        } catch (final IllegalArgumentException e) {
            Log.framework().error("IllegalArgument calling url constructor.", e);
        } catch (final SecurityException e) {
            Log.framework().error("SecurityException calling url constructor.", e);
        } catch (final InstantiationException e) {
            Log.framework().error("InstantiationException calling url constructor.", e);
        } catch (final IllegalAccessException e) {
            Log.framework().error("IllegalAccessException calling url constructor.", e);
        } catch (final InvocationTargetException e) {
            Log.framework().error("InvocationTargetException calling url constructor.", e);
        } catch (final NoSuchMethodException e) {
            Log.framework().error("NoSuchMethodException calling url constructor.", e);
        }

        session.notifyError(Context.tr("Unknow page: ") + pageCode);
        return new PageNotFound(null);
    }

    /**
     * Return the session for the user. Either an existing session or a new session.
     *
     * @param header
     * @return the session matching the user
     */
    private Session findSession(final HttpHeader header) {
        final String key = header.getHttpCookie().get("session_key");
        Session sessionByKey = null;
        if (key != null && (sessionByKey = SessionManager.getByKey(key)) != null) {
            if (sessionByKey.isExpired()) {
                SessionManager.destroySession(sessionByKey);
                // A new session will be create
            } else {
                sessionByKey.resetExpirationTime();
                return sessionByKey;
            }
        }
        return SessionManager.createSession();
    }

}
