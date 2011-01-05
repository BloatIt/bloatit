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

package com.bloatit.web.html.pages;

import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.SpecialsPageUrl;

@ParamContainer("special")
public class SpecialsPage extends Page {

    public SpecialsPage(final SpecialsPageUrl specialsPageUrl) throws RedirectException {
        super(specialsPageUrl);
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(Context.tr("Special pages"), 2);
        pageTitle.setCssClass("page_title");

        final HtmlList pageList = new HtmlList();

        final HtmlLink memeHtmlLink = new MembersListPageUrl().getHtmlLink(Context.tr("Members list"));

        pageList.add(memeHtmlLink);

        // pageTitle.add(pageList);
        // TODO : fix title

    }

    @Override
    public String getTitle() {
        return "Special pages list";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
