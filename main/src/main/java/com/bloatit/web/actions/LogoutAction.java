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

package com.bloatit.web.actions;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.LogoutActionUrl;

/**
 * A response to a form used to log out of the website
 */
@ParamContainer("action/logout")
public final class LogoutAction extends LoggedAction {
    private final LogoutActionUrl url;

    public LogoutAction(final LogoutActionUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        session.setAuthToken(null);
        session.notifyGood(Context.tr("Logout success."));
        return session.pickPreferredPage();
    }

    @Override
    public Url doProcessErrors() {
        session.notifyList(url.getMessages());
        return new IndexPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You should log in before you log out ..." + "Note : if you log in using the form below, you'll be immediatly"
                + "logged out (yes this is a stupid behavior, but we try to use generic code" + "... sometimes generic meands stupid ;)");
    }

    @Override
    protected void transmitParameters() {
        // Nothing to save
    }
}
