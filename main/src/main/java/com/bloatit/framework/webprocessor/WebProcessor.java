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
import com.bloatit.framework.xcgiserver.HttpReponseField.StatusCode;
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
                final String pageCode = httpHeader.getPageName();
                final String requestLanguage = httpHeader.getPageLanguage();

                if (requestLanguage.equalsIgnoreCase(HttpHeader.DEFAULT_LANG)) {
                    // Redirect to same page but with language code set
                    final String redirectUrl = createLowerCaseUrl(Context.getLocalizator().getLanguageCode(), httpHeader.getQueryString(), pageCode);
                    response.writeRedirect(StatusCode.REDIRECTION_307_TEMPORARY_REDIRECT, redirectUrl);
                } else if (pageCode.matches(".*[A-Z].*")) {
                    // Redirect to same page but in lower case
                    response.writeRedirect(StatusCode.REDIRECTION_300_MULTIPLE_CHOICES,
                                           createLowerCaseUrl(httpHeader.getPageLanguage(), httpHeader.getQueryString(), pageCode));
                } else {
                    // Normal case !
                    ModelAccessor.authenticate(key);
                    final Linkable linkable = constructLinkable(pageCode, parameters, session);
                    linkable.writeToHttp(response, this);
                }
            } catch (final ShallNotPassException e) {
                try {
                    Log.framework().fatal("Right management error", e);
                    final Linkable linkable = constructLinkable(PageForbiddenUrl.getPageName(), parameters, session);
                    linkable.writeToHttp(response, this);
                } catch (final RedirectException e1) {
                    throw new ExternalErrorException("Cannot create error page after and error in right management.", e1);
                } finally {
                    try {
                        ModelAccessor.rollback();
                    } catch (final RuntimeException e1) {
                        Log.framework().fatal("Couldn't rollback after shall not pass", e);
                        throw e1;
                    }
                }
            } catch (final PageNotFoundException e) {
                Log.framework().info("Page not found", e);
                final Linkable linkable = constructLinkable(PageNotFoundUrl.getName(), parameters, session);
                try {
                    linkable.writeToHttp(response, this);
                } catch (final RedirectException e1) {
                    Log.framework().info("Redirect to " + e.getUrl(), e);
                    response.writeRedirect(StatusCode.REDIRECTION_301_MOVED_PERMANENTLY, e.getUrl().urlString());
                }
            } catch (final RedirectException e) {
                Log.framework().info("Redirect to " + e.getUrl(), e);
                response.writeRedirect(StatusCode.REDIRECTION_303_SEE_OTHER, e.getUrl().urlString());
            }
        } catch (final RuntimeException e) {
            response.writeException(e);
            try {
                ModelAccessor.rollback();
            } catch (final RuntimeException e1) {
                Log.framework().fatal("Couldn't rollback after runtime exception", e);
                throw e1;
            }
            throw e;
        } finally {
            try {
                ModelAccessor.close();
            } catch (final RuntimeException e) {
                Log.framework().fatal("Couldn't close model", e);
                throw e;
            }
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
