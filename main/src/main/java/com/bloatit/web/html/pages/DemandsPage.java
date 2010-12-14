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


import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.demand.DemandPage;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.annotations.PageComponent;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class DemandsPage extends Page {

    @PageComponent
    HtmlPagedList<Demand> pagedMemberList;

    public DemandsPage(final Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Demands list"),2);

        final PageIterable<Demand> demandList = DemandManager.getDemands();

        final HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

            UrlBuilder demandPageUrlBuilder = new UrlBuilder(DemandPage.class);

            @Override
            public HtmlNode generate(final Demand demand) {
                demandPageUrlBuilder.addParameter("id", demand);
                return new HtmlListItem(demandPageUrlBuilder.getHtmlLink(demand.getTitle()));
            }
        };

        pagedMemberList = new HtmlPagedList<Demand>(demandItemRenderer, demandList, request, session);

        pageTitle.add(pagedMemberList);

        add(pageTitle);
    }

    @Override
    public String getTitle() {
        return "View all demands - search demands";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    public String isBancale() {
        return "peut-être";
    }
}
