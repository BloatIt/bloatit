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
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.LoginPageUrl;

@ParamContainer(value = "logged", isComponent = true)
public abstract class LoggedPage extends Page {

    private final Url meUrl;

    protected LoggedPage(final Url url) {
        super(url);
        this.meUrl = url;
    }

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

    public abstract HtmlElement createRestrictedContent() throws RedirectException;

    public abstract String getRefusalReason();

}
