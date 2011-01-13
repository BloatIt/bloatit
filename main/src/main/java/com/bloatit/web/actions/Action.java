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

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.Url;
import com.bloatit.web.utils.url.UrlNode;

/**
 * The mother of all actions
 */
public abstract class Action implements Linkable {

    protected final Session session;
    private final UrlNode actionUrl;

    public Action(final UrlNode url) {
        this.actionUrl = url;
        session = Context.getSession();
    }

    public final Url process() throws RedirectException {
        if (actionUrl.getMessages().hasMessage(Level.ERROR)) {
            return doProcessErrors();
        }
        return doProcess();
    }

    protected abstract Url doProcess() throws RedirectException;

    protected abstract Url doProcessErrors() throws RedirectException;
}
