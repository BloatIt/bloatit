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

// import java.util.Random;
import com.bloatit.data.search.DemandSearch;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlLink;
import com.bloatit.framework.webserver.components.HtmlRenderer;
import com.bloatit.framework.webserver.components.HtmlTitle;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlForm.Method;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlNode;
import com.bloatit.model.demand.Demand;
import com.bloatit.web.components.HtmlDemandSumary;
import com.bloatit.web.components.HtmlPagedList;
import com.bloatit.web.pages.master.Page;
import com.bloatit.web.url.DemandListUrl;

@ParamContainer("demand/list")
public final class DemandList extends Page {

    public static final String SEARCH_STRING = "search_string";
    @RequestParam(defaultValue = "", name = SEARCH_STRING)
    private final String searchString;

    private HtmlPagedList<Demand> pagedDemandList;
    private final DemandListUrl url;

    public DemandList(final DemandListUrl url) {
        super(url);
        this.url = url;
        this.searchString = url.getSearchString();

        generateContent();
    }

    private void generateContent() {

        // Search block

        // ////////////////////
        // Div demand_search_block
        final HtmlDiv demandSearchBlock = new HtmlDiv("demand_search_block");
        {
            final HtmlTitle pageTitle = new HtmlTitle(Context.tr("Search a demand"), 1);
            demandSearchBlock.add(pageTitle);

            final HtmlForm searchForm = new HtmlForm(new DemandListUrl().urlString(), Method.GET);
            {
                final HtmlTextField searchField = new HtmlTextField(SEARCH_STRING);
                searchField.setDefaultValue(searchString);

                final HtmlSubmit searchButton = new HtmlSubmit(Context.trc("Search (verb)", "Search"));

                searchForm.add(searchField);
                searchForm.add(searchButton);
            }
            demandSearchBlock.add(searchForm);

            final HtmlDiv demandFilter = new HtmlDiv("demand_filter");
            {
                final DemandListUrl allFilterUrl = url.clone();
                final HtmlLink allFilter = allFilterUrl.getHtmlLink(Context.tr("all"));

                final DemandListUrl preparingFilterUrl = url.clone();
                final HtmlLink preparingFilter = preparingFilterUrl.getHtmlLink(Context.tr("in progress"));

                final DemandListUrl finishedFilterUrl = url.clone();
                final HtmlLink finishedFilter = finishedFilterUrl.getHtmlLink(Context.tr("finished"));

                demandFilter.addText(Context.tr("Filter: "));
                demandFilter.add(allFilter);
                demandFilter.addText(" – ");
                demandFilter.add(preparingFilter);
                demandFilter.addText(" – ");
                demandFilter.add(finishedFilter);
            }
            demandSearchBlock.add(demandFilter);

            final HtmlDiv demandSort = new HtmlDiv("demand_sort");
            {
                final DemandListUrl contributionSortUrl = url.clone();
                final HtmlLink contributionSort = contributionSortUrl.getHtmlLink(Context.tr("contribution"));

                final DemandListUrl popularitySortUrl = url.clone();
                final HtmlLink popularitySort = popularitySortUrl.getHtmlLink(Context.tr("popularity"));

                final DemandListUrl progressSortUrl = url.clone();
                final HtmlLink progressSort = progressSortUrl.getHtmlLink(Context.tr("progress"));

                final DemandListUrl creationDateSortUrl = url.clone();
                final HtmlLink creationDateSort = creationDateSortUrl.getHtmlLink(Context.tr("creation date"));

                final DemandListUrl expirationDateSortUrl = url.clone();
                final HtmlLink expirationDateSort = expirationDateSortUrl.getHtmlLink(Context.tr("expiration date"));

                demandSort.addText(Context.tr("Sort by: "));
                demandSort.add(contributionSort);
                demandSort.addText(" – ");
                demandSort.add(popularitySort);
                demandSort.addText(" – ");
                demandSort.add(progressSort);
                demandSort.addText(" – ");
                demandSort.add(creationDateSort);
                demandSort.addText(" – ");
                demandSort.add(expirationDateSort);

            }
            demandSearchBlock.add(demandSort);

            final HtmlDiv demandOrder = new HtmlDiv("demand_order");
            {
                final DemandListUrl ascendingOrderUrl = url.clone();
                final HtmlLink ascendingOrder = ascendingOrderUrl.getHtmlLink(Context.tr("ascending"));

                final DemandListUrl descendingOrderUrl = url.clone();
                final HtmlLink descendingOrder = descendingOrderUrl.getHtmlLink(Context.tr("descending"));

                demandOrder.addText(Context.tr("Order: "));
                demandOrder.add(ascendingOrder);
                demandOrder.addText(" – ");
                demandOrder.add(descendingOrder);
            }
            demandSearchBlock.add(demandOrder);

            // ////////////////////
            // Div demand_advanced_search_button
            final HtmlDiv demandAdvancedSearchButton = new HtmlDiv("demand_advanced_search_button");
            {
                final HtmlLink showHideShareBlock = new HtmlLink("javascript:showHide('demand_advanced_search')", Context.tr("+ Advanced search"));
                demandAdvancedSearchButton.add(showHideShareBlock);
            }
            demandSearchBlock.add(demandAdvancedSearchButton);

            // ////////////////////
            // Div demand_advanced_search
            final HtmlDiv demandAdvancedSearch = new HtmlDiv("demand_advanced_search", "demand_advanced_search");
            {

            }
            demandSearchBlock.add(demandAdvancedSearch);
        }
        add(demandSearchBlock);

        // Demand list

        final PageIterable<Demand> ideaList;

        DemandSearch search = new DemandSearch(searchString);
        ideaList = search.search();

        final HtmlRenderer<Demand> demandItemRenderer = new IdeasListItem();

        final DemandListUrl clonedUrl = url.clone();
        pagedDemandList = new HtmlPagedList<Demand>(demandItemRenderer, ideaList, clonedUrl, clonedUrl.getPagedDemandListUrl());

        add(pagedDemandList);
    }

    @Override
    public String getTitle() {
        return Context.tr("View demands - search demands");
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getCustomCss() {
        return "demands-list.css";
    }

    static class IdeasListItem implements HtmlRenderer<Demand> {

        private Demand demand;

        @Override
        public HtmlNode generate(final Demand demand) {
            this.demand = demand;

            return generateContent();
        }

        private HtmlNode generateContent() {
            return new HtmlDemandSumary(demand);
        }
    };
}
