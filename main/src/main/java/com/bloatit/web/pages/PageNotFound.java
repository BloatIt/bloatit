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
package com.bloatit.web.pages;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.specific.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;

public class PageNotFound extends MasterPage {

    public PageNotFound(final PageNotFoundUrl pageNotFoundUrl) {
        super(pageNotFoundUrl);
    }

    @Override
    protected String getPageTitle() {
        return Context.tr("Page not found");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv box = new HtmlDiv("padding_box");

        final HtmlTitle errorTitle = new HtmlTitle(Context.tr("Page not found"), 2);
        box.add(errorTitle);

        add(box);
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return PageNotFound.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();

        breadcrumb.pushLink(new PageNotFoundUrl().getHtmlLink(tr("Page not found")));

        return breadcrumb;
    }

}
