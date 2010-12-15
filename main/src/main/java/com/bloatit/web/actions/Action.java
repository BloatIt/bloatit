/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.actions;

import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.LoginPage;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.UrlBuilder;
import com.bloatit.web.utils.url.UrlComponent;

public abstract class Action implements Linkable {

    protected final Session session;
    private final UrlComponent actionUrl;

    /**
     * The constructor mustn't thows exception
     * 
     * @param resquest
     */
    public Action(UrlComponent url) {
        this.actionUrl = url;
        session = Context.getSession();
    }

    public final String process() throws RedirectException{
        if (actionUrl.getMessages().hasMessage(Level.ERROR)) {
            session.notifyList(actionUrl.getMessages());
            throw new RedirectException(new UrlBuilder(LoginPage.class).buildUrl());
        }
        return doProcess();
    }
    
    abstract protected String doProcess() throws RedirectException;

}
