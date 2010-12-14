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
import com.bloatit.web.annotations.PageComponent;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.demand.DemandPage;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class GlobalSearchPage extends Page {

    public final static String SEARCH_CODE = "global_search";
    @RequestParam(defaultValue = "vide", name = SEARCH_CODE)
    private String searchString;

    @PageComponent
    private HtmlPagedList<Demand> pagedMemberList;

    public GlobalSearchPage(final Request request) {
        super(request);
        request.setValues(this);
        addNotifications(request.getMessages());
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Search result"),2);

        pageTitle.add(generateSearchBlock());

        final PageIterable<Demand> demandList = DemandManager.search(searchString);

        final HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

            UrlBuilder demandeUrlBuilder = new UrlBuilder(DemandPage.class);

            @Override
            public HtmlNode generate(final Demand demand) {
                demandeUrlBuilder.addParameter("idea", demand);

                return new HtmlListItem(demandeUrlBuilder.getHtmlLink(demand.getTitle()));
            }
        };

        pagedMemberList = new HtmlPagedList<Demand>(demandItemRenderer, demandList, new UrlBuilder(GlobalSearchPage.class, request.getParameters()), session);

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

    String getSearchCode() {
        return "search";
    }

    private HtmlDiv generateSearchBlock() {
        final HtmlDiv searchBlock = new HtmlDiv("global_search_block");

        final HtmlForm searchForm = new HtmlForm(new UrlBuilder(GlobalSearchPage.class).buildUrl(), HtmlForm.Method.GET);

        final HtmlTextField searchField = new HtmlTextField(GlobalSearchPage.SEARCH_CODE);

        final HtmlButton searchButton = new HtmlButton(session.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);

        return searchBlock;
    }
}
