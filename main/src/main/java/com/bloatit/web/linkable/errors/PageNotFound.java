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
package com.bloatit.web.linkable.errors;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.HttpResponse.StatusCode;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.IndexPageUrl;

public class PageNotFound extends MasterPage {
    private PageNotFoundUrl url;

    public PageNotFound(final PageNotFoundUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Page not found");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true, url);
        final HtmlDiv box = new HtmlDiv("page_not_found");
        layout.addLeft(box);

        final HtmlTitle errorTitle = new HtmlTitle(Context.tr("Page not found"), 1);
        box.add(errorTitle);

        final HtmlParagraph error = new HtmlParagraph();
        String tr = Context.tr("The page you are looking for is not here. Maybe you can try heading back to the <0::Home page>.");
        HtmlMixedText mixed = new HtmlMixedText(tr, new IndexPageUrl().getHtmlLink());
        error.add(mixed);
        box.add(error);

        final HtmlParagraph bug = new HtmlParagraph();
        String tr2 = Context.tr("If you ended up here because of a bug, please report it using the box on the right of the screen.");
        HtmlMixedText mixedExplain = new HtmlMixedText(tr2, new IndexPageUrl().getHtmlLink());
        bug.add(mixedExplain);
        box.add(bug);

        return layout;
    }

    @Override
    protected StatusCode getResponseStatus() {
        return StatusCode.ERROR_404_NOT_FOUND;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return PageNotFound.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new PageNotFoundUrl().getHtmlLink(tr("Page not found")));
        return breadcrumb;
    }

}
