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

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.utils.Parameters;
import com.bloatit.framework.webserver.masters.HttpResponse;
import com.bloatit.framework.webserver.masters.Linkable;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

public abstract class WebServer implements XcgiProcessor {

    public WebServer() {
    }

    @Override
    public final boolean process(final HttpHeader header, final HttpPost post, final HttpResponse response) throws IOException {
        final Session session = findSession(header);

        try {
            Context.reInitializeContext(header, session);

            final String pageCode = header.getQueryString().getPageName();

            // Merge post and get parameters.
            final Parameters parameters = new Parameters();
            parameters.putAll(header.getQueryString().getParameters());
            parameters.putAll(header.getQueryString().getGetParameters());
            parameters.putAll(post.getParameters());

            try {
                ModelAccessor.open();
                final Linkable linkable = constructLinkable(pageCode, parameters, session);
                linkable.writeToHttp(response);
            } catch (PageNotFoundException e) {
                Log.framework().info("Page not found", e);
                final Linkable linkable = constructLinkable(PageNotFoundUrl.getName(), parameters, session);
                try {
                    linkable.writeToHttp(response);
                } catch (RedirectException e1) {
                    Log.framework().info("Redirect to " + e.getUrl(), e);
                    response.writeRedirect(e.getUrl().urlString());
                }
            } catch (final RedirectException e) {
                Log.framework().info("Redirect to " + e.getUrl(), e);
                response.writeRedirect(e.getUrl().urlString());
            } finally {
                ModelAccessor.close();
            }

        } catch (RuntimeException e) {
            response.writeException(e);
            throw e;
        }
        return true;
    }

    protected abstract Linkable constructLinkable(final String pageCode, final Parameters params, final Session session);

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
