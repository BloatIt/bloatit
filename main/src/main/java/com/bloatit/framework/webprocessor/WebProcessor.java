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
import com.bloatit.framework.utils.parameters.Parameters;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.context.SessionManager;
import com.bloatit.framework.webprocessor.masters.Linkable;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.xcgiserver.HttpHeader;
import com.bloatit.framework.xcgiserver.HttpPost;
import com.bloatit.framework.xcgiserver.HttpResponse;
import com.bloatit.framework.xcgiserver.RequestKey;
import com.bloatit.framework.xcgiserver.XcgiProcessor;

public abstract class WebProcessor implements XcgiProcessor {

    public WebProcessor() {
        super();
    }

    @Override
    public final boolean process(final RequestKey key, final HttpHeader httpHeader, final HttpPost post, final HttpResponse response)
            throws IOException {
        try {
            // Merge post and get parameters.
            final Parameters parameters = new Parameters();
            parameters.putAll(httpHeader.getGetParameters());
            parameters.putAll(post.getParameters());

            ModelAccessor.open();
            final Session session = SessionManager.getOrCreateSession(key);
            Context.reInitializeContext(httpHeader, session);

            try {

                // If pagename contains upper case char then redirect to the
                // same page with lower case.
                final String pageCode = httpHeader.getPageName();
                if (pageCode.matches(".*[A-Z].*")) {
                    response.writeRedirect(createLowerCaseUrl(httpHeader.getLanguage(), httpHeader.getQueryString(), pageCode));
                } else {

                    ModelAccessor.authenticate(key);

                    // Normal case !
                    final Linkable linkable = constructLinkable(pageCode, parameters, session);
                    linkable.writeToHttp(response, this);

                }
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
            } finally {
                ModelAccessor.close();
            }

        } catch (final RuntimeException e) {
            response.writeException(e);
            try {
                ModelAccessor.rollback();
            } catch (final RuntimeException e1) {
                Log.framework().fatal("Unknown error", e);
                throw e1;
            }
            throw e;
        }
        return true;
    }

    private String createLowerCaseUrl(final String language, final String queryString, final String pageCode) {
        final StringBuilder url = new StringBuilder();
        url.append("/");
        url.append(language);
        url.append("/");
        url.append(pageCode.toLowerCase());
        if (queryString.length() > 0) {
            url.append("?");
            url.append(queryString);
        }
        return url.toString();
    }

    public abstract Linkable constructLinkable(final String pageCode, final Parameters postGetParameters, final Session session);

}
