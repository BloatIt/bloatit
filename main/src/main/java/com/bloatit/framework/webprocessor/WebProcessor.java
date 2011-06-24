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
package com.bloatit.framework.webprocessor;

import java.io.IOException;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.Hash;
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.context.WebHeader;
import com.bloatit.framework.webprocessor.masters.Linkable;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.HttpResponse;
import com.bloatit.framework.xcgiserver.SessionKey.WrongSessionKeyFormatException;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

public abstract class WebProcessor implements XcgiProcessor {

    public WebProcessor() {
        super();
    }

    @Override
    public final boolean process(final HttpHeader httpHeader, final HttpPost post, final HttpResponse response) throws IOException {
        final Session session = findSession(httpHeader);

        try {
            final WebHeader header = new WebHeader(httpHeader);

            ModelAccessor.open();
            Context.reInitializeContext(header, session);

            // Access log
            final String memberId = session.getUserToken().isAuthenticated() ? session.getUserToken().getMember().getId().toString() : "-1";
            final String sessionKey = Hash.shortHash(session.getShortKey());
            Log.framework().info("Access:Context: " + //
                    "USER_ID=\"" + memberId + //
                    "\"; KEY=\"" + sessionKey + //
                    "\"; LANG=\"" + Context.getLocalizator().getLocale() + "\"");

            final String pageCode = header.getPageName();

            // Merge post and get parameters.
            final Parameters parameters = new Parameters();
            parameters.putAll(header.getParameters());
            parameters.putAll(header.getGetParameters());
            parameters.putAll(post.getParameters());

            try {
                final Linkable linkable = constructLinkable(pageCode.toLowerCase(), parameters, session);
                linkable.writeToHttp(response, this);
            } catch (final ShallNotPassException e) {
                Log.framework().fatal("Right management error", e);
                final Linkable linkable = constructLinkable(PageForbiddenUrl.getPageName(), parameters, session);
                try {
                    linkable.writeToHttp(response, this);
                } catch (final RedirectException e1) {
                    throw new ExternalErrorException("Cannot create error page after and error in right management.", e1);
                }
            } catch (final PageNotFoundException e) {
                Log.framework().info("Page not found", e);
                final Linkable linkable = constructLinkable(PageNotFoundUrl.getName(), parameters, session);
                try {
                    linkable.writeToHttp(response, this);
                } catch (final RedirectException e1) {
                    Log.framework().info("Redirect to " + e.getUrl(), e);
                    response.writeRedirect(e.getUrl().urlString());
                }
            } catch (final RedirectException e) {
                Log.framework().info("Redirect to " + e.getUrl(), e);
                response.writeRedirect(e.getUrl().urlString());
            }
            ModelAccessor.close();

        } catch (final RuntimeException e) {
            response.writeException(e);
            try {
                ModelAccessor.rollback();
            } catch (final RuntimeException e1) {
                Log.framework().fatal(e);
                throw e1;
            }
            throw e;
        }
        return true;
    }

    public abstract Linkable constructLinkable(final String pageCode, final Parameters postGetParameters, final Session session);

    /**
     * Return the session for the user. Either an existing session or a new
     * session.
     * 
     * @param header
     * @return the session matching the user
     */
    private Session findSession(final HttpHeader header) {
        final String key = header.getHttpCookie().get("session_key");
        Session sessionByKey = null;
        try {
            if (key != null && (sessionByKey = SessionManager.getByKey(key, header.getRemoteAddr())) != null) {
                if (sessionByKey.isExpired()) {
                    SessionManager.destroySession(sessionByKey);
                    // A new session will be create
                } else {
                    sessionByKey.resetExpirationTime();
                    return sessionByKey;
                }
            }
        } catch (WrongSessionKeyFormatException e) {
            //Just don't restore session
        }
        return SessionManager.createSession(header.getRemoteAddr());
    }

}
