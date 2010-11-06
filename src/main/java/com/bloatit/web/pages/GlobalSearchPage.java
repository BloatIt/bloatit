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
package com.bloatit.web.pages;

import java.util.HashMap;
import java.util.Map;

import com.bloatit.common.PageIterable;
import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.htmlrenderer.HtmlResult;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlListItem;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPagedList;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlRenderer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class GlobalSearchPage extends Page {

    public GlobalSearchPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public GlobalSearchPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected HtmlComponent generateContent() {

        final HtmlTitle pageTitle = new HtmlTitle(session.tr("Search result"), "");

        HtmlBlock searchBlock = new HtmlBlock("global_search_block");
        generateSearchBlock(searchBlock);

        pageTitle.add(searchBlock);

        if (parameters.containsKey(getSearchCode())) {
            outputParameters.put(getSearchCode(), parameters.get(getSearchCode()));

            String searchString = parameters.get(getSearchCode());

            final PageIterable<Demand> demandList = DemandManager.search(searchString);
            System.err.println("demandList " + demandList.size());
            System.err.println("params " + parameters);
            HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

                @Override
                public void generate(HtmlResult htmlResult, Demand demand) {
                    final DemandPage demandPage = new DemandPage(session, demand);
                    final HtmlListItem item = new HtmlListItem(HtmlTools.generateLink(session, demand.getTitle(), demandPage));
                    item.generate(htmlResult);
                }
            };

            HtmlPagedList<Demand> pagedMemberList = new HtmlPagedList<Demand>(demandItemRenderer, demandList, this, session);

            int pageSize = 10;
            int currentPage = 0;

            if (parameters.containsKey("page_size")) {
                try {
                    pageSize = Integer.parseInt(parameters.get("page_size"));
                } catch (NumberFormatException e) {
                }
            }

            if (parameters.containsKey("page")) {
                try {
                    currentPage = Integer.parseInt(parameters.get("page")) - 1;
                } catch (NumberFormatException e) {
                }
            }

            pagedMemberList.setPageSize(pageSize);
            pagedMemberList.setCurrentPage(currentPage);

            pageTitle.add(pagedMemberList);

        }

        return pageTitle;

    }

    @Override
    public String getTitle() {
        return "View all demands - search demands";
    }

    @Override
    public String getCode() {
        return "global_search";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    String getSearchCode() {
        return "search";
    }

    private void generateSearchBlock(HtmlBlock searchBlock) {
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(session);
        HtmlForm searchForm = new HtmlForm(globalSearchPage);
        searchForm.setMethod(HtmlForm.Method.GET);
        HtmlTextField searchField = new HtmlTextField();
        searchField.setName(globalSearchPage.getSearchCode());

        if (parameters.containsKey(getSearchCode())) {
            searchField.setDefaultValue(parameters.get(getSearchCode()));
        }

        HtmlButton searchButton = new HtmlButton(session.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);
    }
}
