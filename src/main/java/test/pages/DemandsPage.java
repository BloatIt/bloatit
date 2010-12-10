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


import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.utils.PageComponent;
import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.html.HtmlNode;
import test.html.components.advanced.HtmlPagedList;
import test.html.components.standard.HtmlListItem;
import test.html.components.standard.HtmlRenderer;
import test.html.components.standard.HtmlTitleBlock;
import test.pages.demand.DemandPage;
import test.pages.master.Page;

public class DemandsPage extends Page {

    @PageComponent
    HtmlPagedList<Demand> pagedMemberList;

    public DemandsPage(Request request) throws RedirectException {
        super(request);
        generateContent();
    }
    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Demands list"));

        final PageIterable<Demand> demandList = DemandManager.getDemands();

        HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

            UrlBuilder demandPageUrlBuilder = new UrlBuilder(DemandPage.class);

            @Override
            public HtmlNode generate(Demand demand) {
                demandPageUrlBuilder.addParameter("idea", demand);
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
