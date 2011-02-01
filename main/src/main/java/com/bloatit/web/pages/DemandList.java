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
import com.bloatit.data.DaoDemand.DemandState;
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

    public static final String SEARCH_STRING_CODE = "search_string";
    @RequestParam(defaultValue = "", name = SEARCH_STRING_CODE)
    private final String searchString;

    public static final String FILTER_ALL = "all";
    public static final String FILTER_IN_PROGRESS = "in progress";
    public static final String FILTER_FINISHED = "finished";
    public static final String FILTER_CODE = "filter";
    @RequestParam(defaultValue = "all", name = FILTER_CODE)
    private final String filter;

    public static final String SORT_BY_CONTRIBUTION = "contribution";
    public static final String SORT_BY_PROGRESS = "progress";
    public static final String SORT_BY_POPULARITY = "popularity";
    public static final String SORT_BY_CREATION_DATE = "creation date";
    public static final String SORT_BY_EXPIRATION_DATE = "expiration date";
    public static final String SORT_CODE = "sort";
    @RequestParam(defaultValue = "popularity", name = SORT_CODE)
    private final String sort;

    public static final String ORDER_ASC = "asc";
    public static final String ORDER_DESC = "desc";
    public static final String ORDER_CODE = "order";
    @RequestParam(defaultValue = "desc", name = ORDER_CODE)
    private final String order;

    private HtmlPagedList<Demand> pagedDemandList;
    private final DemandListUrl url;

    public DemandList(final DemandListUrl url) {
        super(url);
        this.url = url;
        this.searchString = url.getSearchString();
        this.filter = url.getFilter();
        this.sort = url.getSort();
        this.order = url.getOrder();

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

            DemandListUrl formUrl = url.clone();
            formUrl.setSearchString("");
            final HtmlForm searchForm = new HtmlForm(formUrl.urlString(), Method.GET);
            {
                final HtmlTextField searchField = new HtmlTextField(SEARCH_STRING_CODE);
                searchField.setDefaultValue(searchString);

                final HtmlSubmit searchButton = new HtmlSubmit(Context.trc("Search (verb)", "Search"));

                searchForm.add(searchField);
                searchForm.add(searchButton);
            }
            demandSearchBlock.add(searchForm);

            final HtmlDiv demandFilter = new HtmlDiv("demand_filter");
            {
                final DemandListUrl allFilterUrl = url.clone();
                allFilterUrl.setFilter(FILTER_ALL);
                final HtmlLink allFilter = allFilterUrl.getHtmlLink(Context.tr("all"));
                if(filter.equals(FILTER_ALL)) {
                    allFilter.setCssClass("selected");
                }

                final DemandListUrl preparingFilterUrl = url.clone();
                preparingFilterUrl.setFilter(FILTER_IN_PROGRESS);
                final HtmlLink preparingFilter = preparingFilterUrl.getHtmlLink(Context.tr("in progress"));
                if(filter.equals(FILTER_IN_PROGRESS)) {
                    preparingFilter.setCssClass("selected");
                }

                final DemandListUrl finishedFilterUrl = url.clone();
                finishedFilterUrl.setFilter(FILTER_FINISHED);
                final HtmlLink finishedFilter = finishedFilterUrl.getHtmlLink(Context.tr("finished"));
                if(filter.equals(FILTER_FINISHED)) {
                    finishedFilter.setCssClass("selected");
                }

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
                final DemandListUrl popularitySortUrl = url.clone();
                popularitySortUrl.setSort(SORT_BY_POPULARITY);
                final HtmlLink popularitySort = popularitySortUrl.getHtmlLink(Context.tr("popularity"));
                if(sort.equals(SORT_BY_POPULARITY)) {
                    popularitySort.setCssClass("selected");
                }

                final DemandListUrl contributionSortUrl = url.clone();
                contributionSortUrl.setSort(SORT_BY_CONTRIBUTION);
                final HtmlLink contributionSort = contributionSortUrl.getHtmlLink(Context.tr("contribution"));
                if(sort.equals(SORT_BY_CONTRIBUTION)) {
                    contributionSort.setCssClass("selected");
                }

                final DemandListUrl progressSortUrl = url.clone();
                progressSortUrl.setSort(SORT_BY_PROGRESS);
                final HtmlLink progressSort = progressSortUrl.getHtmlLink(Context.tr("progress"));
                if(sort.equals(SORT_BY_PROGRESS)) {
                    progressSort.setCssClass("selected");
                }

                final DemandListUrl creationDateSortUrl = url.clone();
                creationDateSortUrl.setSort(SORT_BY_CREATION_DATE);
                final HtmlLink creationDateSort = creationDateSortUrl.getHtmlLink(Context.tr("creation date"));
                if(sort.equals(SORT_BY_CREATION_DATE)) {
                    creationDateSort.setCssClass("selected");
                }

                final DemandListUrl expirationDateSortUrl = url.clone();
                expirationDateSortUrl.setSort(SORT_BY_EXPIRATION_DATE);
                final HtmlLink expirationDateSort = expirationDateSortUrl.getHtmlLink(Context.tr("expiration date"));
                if(sort.equals(SORT_BY_EXPIRATION_DATE)) {
                    expirationDateSort.setCssClass("selected");
                }

                demandSort.addText(Context.tr("Sort by: "));
                demandSort.add(popularitySort);
                demandSort.addText(" – ");
                demandSort.add(contributionSort);
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

                final DemandListUrl descendingOrderUrl = url.clone();
                descendingOrderUrl.setOrder(ORDER_DESC);
                final HtmlLink descendingOrder = descendingOrderUrl.getHtmlLink(Context.tr("descending"));
                if(order.equals(ORDER_DESC)) {
                    descendingOrder.setCssClass("selected");
                }

                final DemandListUrl ascendingOrderUrl = url.clone();
                ascendingOrderUrl.setOrder(ORDER_ASC);
                final HtmlLink ascendingOrder = ascendingOrderUrl.getHtmlLink(Context.tr("ascending"));
                if(order.equals(ORDER_ASC)) {
                    ascendingOrder.setCssClass("selected");
                }

                demandOrder.addText(Context.tr("Order: "));
                demandOrder.add(descendingOrder);
                demandOrder.addText(" – ");
                demandOrder.add(ascendingOrder);
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
        final HtmlRenderer<Demand> demandItemRenderer = new IdeasListItem();

        final DemandListUrl clonedUrl = url.clone();
        pagedDemandList = new HtmlPagedList<Demand>(demandItemRenderer, searchResult(), clonedUrl, clonedUrl.getPagedDemandListUrl());

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


    private PageIterable<Demand> searchResult() {

        DemandSearch search = new DemandSearch(searchString);

        if(!filter.equals(FILTER_ALL)) {
            if(filter.equals(FILTER_IN_PROGRESS)) {
                search.addDemandStateFilter(DemandState.FINISHED);
                search.addDemandStateFilter(DemandState.DISCARDED);
            } else if(filter.equals(FILTER_FINISHED)) {
                search.addDemandStateFilter(DemandState.DEVELOPPING);
                search.addDemandStateFilter(DemandState.INCOME);
                search.addDemandStateFilter(DemandState.PENDING);
                search.addDemandStateFilter(DemandState.PREPARING);
            }
        }


        return search.search();
    }
}
