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

// import java.util.Random;
import com.bloatit.common.PageIterable;
import com.bloatit.framework.demand.Demand;
import com.bloatit.framework.demand.DemandManager;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.components.custom.HtmlDemandSumary;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitle;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlForm.Method;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.DemandListUrl;

@ParamContainer("demand/list")
public final class DemandList extends Page {

    private HtmlPagedList<Demand> pagedDemandList;
    private final DemandListUrl url;

    public DemandList(final DemandListUrl url) {
        super(url);
        this.url = url;

        generateContent();
    }

    private void generateContent() {

        //Search block

        //////////////////////
        // Div demand_search_block
        final HtmlDiv demandSearchBlock = new HtmlDiv("demand_search_block");
        {
            final HtmlTitle pageTitle = new HtmlTitle(Context.tr("Search a demand"), 1);
            demandSearchBlock.add(pageTitle);

            final HtmlForm searchForm = new HtmlForm(new DemandListUrl().toString(), Method.GET);
            {
                HtmlTextField searchField = new HtmlTextField("search_string");
                HtmlSubmit  searchButton = new HtmlSubmit(Context.trc("Search (verb)", "Search"));

                searchForm.add(searchField);
                searchForm.add(searchButton);
            }
            demandSearchBlock.add(searchForm);


            final HtmlDiv demandFilter= new HtmlDiv("demand_filter");
            {
                DemandListUrl allFilterUrl = url.clone();
                HtmlLink allFilter = allFilterUrl.getHtmlLink(Context.tr("all"));

                DemandListUrl preparingFilterUrl = url.clone();
                HtmlLink preparingFilter = preparingFilterUrl.getHtmlLink(Context.tr("preparing"));

                DemandListUrl finishedFilterUrl = url.clone();
                HtmlLink finishedFilter = finishedFilterUrl.getHtmlLink(Context.tr("finished"));



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
                DemandListUrl contributionSortUrl = url.clone();
                HtmlLink contributionSort = contributionSortUrl.getHtmlLink(Context.tr("contribution"));

                DemandListUrl popularitySortUrl = url.clone();
                HtmlLink popularitySort = popularitySortUrl.getHtmlLink(Context.tr("popularity"));

                DemandListUrl progressSortUrl = url.clone();
                HtmlLink progressSort = progressSortUrl.getHtmlLink(Context.tr("progress"));

                DemandListUrl creationDateSortUrl = url.clone();
                HtmlLink creationDateSort = creationDateSortUrl.getHtmlLink(Context.tr("creation date"));

                DemandListUrl expirationDateSortUrl = url.clone();
                HtmlLink expirationDateSort = expirationDateSortUrl.getHtmlLink(Context.tr("expiration date"));


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
                DemandListUrl ascendingOrderUrl = url.clone();
                HtmlLink ascendingOrder = ascendingOrderUrl.getHtmlLink(Context.tr("ascending"));

                DemandListUrl descendingOrderUrl = url.clone();
                HtmlLink descendingOrder = descendingOrderUrl.getHtmlLink(Context.tr("descending"));



                demandOrder.addText(Context.tr("Order: "));
                demandOrder.add(ascendingOrder);
                demandOrder.addText(" – ");
                demandOrder.add(descendingOrder);
            }
            demandSearchBlock.add(demandOrder);

            //////////////////////
            // Div demand_advanced_search_button
            final HtmlDiv demandAdvancedSearchButton = new HtmlDiv("demand_advanced_search_button");
            {
                HtmlLink showHideShareBlock = new HtmlLink("javascript:showHide('demand_advanced_search')", Context.tr("+ Advanced search"));
                demandAdvancedSearchButton.add(showHideShareBlock);
            }
            demandSearchBlock.add(demandAdvancedSearchButton);

            //////////////////////
            // Div demand_advanced_search
            final HtmlDiv demandAdvancedSearch = new HtmlDiv("demand_advanced_search", "demand_advanced_search");
            {


            }
            demandSearchBlock.add(demandAdvancedSearch);
        }
        add(demandSearchBlock);





        //Demand list

        final PageIterable<Demand> ideaList = DemandManager.getDemands();

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
