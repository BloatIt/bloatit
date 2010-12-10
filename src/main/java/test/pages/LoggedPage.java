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

package test.pages;

import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.html.HtmlElement;
import test.pages.master.Page;

public abstract class LoggedPage extends Page {

    protected LoggedPage(final Request request) throws RedirectException {
        super(request);

    }

    @Override
    public String process() throws RedirectException {
        final String retValue = super.process();

        if (session.isLogged()) {
            add(generateRestrictedContent());
        } else {

            session.notifyBad(getRefusalReason());
            session.setTargetPage(new UrlBuilder(this).buildUrl());
            throw new RedirectException(new UrlBuilder(LoginPage.class).buildUrl());
        }
        return retValue;
    }

    public abstract HtmlElement generateRestrictedContent();

    public abstract String getRefusalReason();

}
