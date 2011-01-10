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

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.annotations.PageComponent;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.GlobalSearchPageUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;

@ParamContainer("search")
public class GlobalSearchPage extends Page {

    public final static String SEARCH_CODE = "global_search";
    @RequestParam(defaultValue = "", name = SEARCH_CODE)
    private final String searchString;

    @PageComponent
    private HtmlPagedList<Demand> pagedMemberList;
    private final GlobalSearchPageUrl url;

    public GlobalSearchPage(final GlobalSearchPageUrl url) {
        super(url);
        this.url = url;
        this.searchString = url.getSearchString();

        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(Context.tr("Search result"), 2);

        pageTitle.add(generateSearchBlock());

        final PageIterable<Demand> demandList = DemandManager.search(searchString);

        final HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

            @Override
            public HtmlNode generate(final Demand demand) {
                return new HtmlListItem(new IdeaPageUrl(demand).getHtmlLink(demand.getTitle()));
            }
        };

        final GlobalSearchPageUrl clonedUrl = url.clone();
        pagedMemberList = new HtmlPagedList<Demand>(demandItemRenderer, demandList, clonedUrl, clonedUrl.getPagedMemberListUrl());

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

        final HtmlForm searchForm = new HtmlForm(new GlobalSearchPageUrl().urlString(), HtmlForm.Method.GET);

        final HtmlTextField searchField = new HtmlTextField(GlobalSearchPage.SEARCH_CODE);

        final HtmlSubmit searchButton = new HtmlSubmit(Context.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);

        return searchBlock;
    }
}
