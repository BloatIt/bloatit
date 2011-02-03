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

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlList;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.SpecialsPageUrl;

@ParamContainer("special")
public final class SpecialsPage extends MasterPage {

    public SpecialsPage(final SpecialsPageUrl specialsPageUrl) {
        super(specialsPageUrl);
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(Context.tr("Special pages"), 2);
        pageTitle.setCssClass("page_title");

        final HtmlList pageList = new HtmlList();

        final HtmlLink memeHtmlLink = new MembersListPageUrl().getHtmlLink(Context.tr("Members list"));

        pageList.add(memeHtmlLink);
    }

    @Override
    public String getPageTitle() {
        return "Special pages list";
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
