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

package com.bloatit.web.html.pages;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer(value = "logged", isComponent = true)
public abstract class LoggedPage extends Page {

    private final Url meUrl;

    protected LoggedPage(Url url) throws RedirectException {
        super(url);
        this.meUrl = url;
    }

    @Override
    public final void create() throws RedirectException {
        super.create();

        if (session.isLogged()) {
            add(createRestrictedContent());
        } else {
            session.notifyBad(getRefusalReason());
            session.setTargetPage(meUrl.urlString());
            throw new RedirectException(new LoginPageUrl().urlString());
        }
    }

    public abstract HtmlElement createRestrictedContent() throws RedirectException;

    public abstract String getRefusalReason();

}
