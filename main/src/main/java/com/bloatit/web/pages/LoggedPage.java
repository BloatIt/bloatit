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

package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.LoginPageUrl;

/**
 * <p>
 * parent class of all pages that require the user to be logged
 * </p>
 * <p>
 * Any page that require login should extend this class and implement the
 * <code>{@link #createRestrictedContent()}</code> and
 * <code>{@link #getRefusalReason()}</code>
 * </p>
 */
public abstract class LoggedPage extends MasterPage {

    /**
     * <p>
     * The URL of this page
     * </p>
     * <p>
     * Note it is private as not usable by children classes that need to get a subclass of
     * URL.
     * </p>
     */
    private final Url meUrl;

    protected LoggedPage(final Url url) {
        super(url);
        this.meUrl = url;
    }

    /**
     * Override the doCreate methods and makes sure the user is logged. If the user is
     * logged, createRestrictedContent is called, otherwise redirects to the
     * <code>LoginPage</code> and use <code>{@link #createRestrictedContent()}</code> to
     * display a warning to the user
     */
    @Override
    protected final void doCreate() throws RedirectException {
        if (session.isLogged()) {
            add(createRestrictedContent());
        } else {
            session.notifyBad(getRefusalReason());
            session.setTargetPage(meUrl);
            throw new RedirectException(new LoginPageUrl());
        }
    }

    /**
     * <p>
     * Creates the content of the page
     * </p>
     * <p>
     * This method is called only when the user is correctly logged. When user is not
     * logged, a redirection to <code>LoginPage</code> will happen, and user will be
     * warned with <code>{@link #getRefusalReason()}</code>
     * </p>
     * 
     * @return the root HtmlElement for the page
     * @throws RedirectException when an error occurs that need to interrupt content
     *         generation and redirect to another page
     */
    public abstract HtmlElement createRestrictedContent() throws RedirectException;

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

}
