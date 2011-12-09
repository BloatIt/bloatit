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

package com.bloatit.web.linkable.master;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.url.LoginPageUrl;

/**
 * <p>
 * parent class of all pages that require the user to be logged
 * </p>
 * <p>
 * Any page that require login should extend this class and implement the <i>
 * {@link #createRestrictedContent(Member)}</i> and <i>
 * {@link #getRefusalReason()}</i>
 * </p>
 */
public abstract class LoggedElveosPage extends ElveosPage {

    public LoggedElveosPage(final Url url) {
        super(url);
    }

    /**
     * Override the doCreate methods and makes sure the user is logged. If the
     * user is logged, createRestrictedContent is called, otherwise redirects to
     * the <i>LoginPage</i> and use <i>{@link #createRestrictedContent(Member)}
     * </i> to display a warning to the user
     */
    @Override
    protected final HtmlElement createBodyContent() throws RedirectException {
        if (AuthToken.isAuthenticated()) {
            try {
                return createRestrictedContent(AuthToken.getMember());
            } catch (final UnauthorizedPrivateAccessException e) {
                throw new ShallNotPassException("Permission error generating page", e);
            }
        }
        getSession().notifyWarning(getRefusalReason());
        getSession().setTargetPage(getUrl());
        throw new RedirectException(new LoginPageUrl());
    }

    /**
     * <p>
     * Creates the content of the page
     * </p>
     * <p>
     * This method is called only when the user is correctly logged. When user
     * is not logged, a redirection to <code>LoginPage</code> will happen, and
     * user will be warned with <code>{@link #getRefusalReason()}</code>
     * </p>
     * 
     * @param loggedUser the current loggedUser. Cannot be null.
     * @return the root HtmlElement for the page
     * @throws RedirectException when an error occurs that need to interrupt
     *             content generation and redirect to another page
     * @throws UnauthorizedPrivateAccessException
     */
    public abstract HtmlElement createRestrictedContent(Member loggedUser) throws RedirectException, UnauthorizedPrivateAccessException;

    /**
     * <p>
     * A method used to warn the user why he couldn't reach the page
     * </p>
     * <p>
     * Standard example :
     * 
     * <pre>
     * public String getRefusalReason() {
     *     return tr(&quot;You need to be logged to access %pagename%&quot;);
     * }
     * </pre>
     * 
     * </p>
     * 
     * @return a String indicating to the user why he cannot access this page
     */
    public abstract String getRefusalReason();

    @Override
    protected final Breadcrumb createBreadcrumb() {
        if (AuthToken.isAuthenticated()) {
            return createBreadcrumb(AuthToken.getMember());
        }
        return createBreadcrumb((Member) null);
    }

    protected abstract Breadcrumb createBreadcrumb(Member loggedUser);
}
