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
import com.bloatit.web.utils.RequestParam;
import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.html.HtmlNode;
import test.html.components.advanced.HtmlPagedList;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlListItem;
import test.html.components.standard.HtmlRenderer;
import test.html.components.standard.HtmlTitleBlock;
import test.html.components.standard.form.HtmlButton;
import test.html.components.standard.form.HtmlForm;
import test.html.components.standard.form.HtmlTextField;
import test.pages.demand.DemandPage;
import test.pages.master.Page;

public class GlobalSearchPage extends Page {

    public final static String SEARCH_CODE = "global_search";
    @RequestParam(defaultValue = "vide", name = SEARCH_CODE)
    private String searchString;

    @PageComponent
    private HtmlPagedList<Demand> pagedMemberList;

    public GlobalSearchPage(Request request) {
        super(request);
        request.setValues(this);
        addNotifications(request.getMessages());
        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Search result"));

        pageTitle.add(generateSearchBlock());

        final PageIterable<Demand> demandList = DemandManager.search(searchString);

        HtmlRenderer<Demand> demandItemRenderer = new HtmlRenderer<Demand>() {

            UrlBuilder demandeUrlBuilder = new UrlBuilder(DemandPage.class);

            @Override
            public HtmlNode generate(Demand demand) {
                demandeUrlBuilder.addParameter("idea", demand);

                return new HtmlListItem( demandeUrlBuilder.getHtmlLink(demand.getTitle()));
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

    String getSearchCode() {
        return "search";
    }

    private HtmlDiv generateSearchBlock( ) {
        HtmlDiv searchBlock = new HtmlDiv("global_search_block");

        HtmlForm searchForm = new HtmlForm(new UrlBuilder(GlobalSearchPage.class).buildUrl() , HtmlForm.Method.GET);

        HtmlTextField searchField = new HtmlTextField(GlobalSearchPage.SEARCH_CODE);

        HtmlButton searchButton = new HtmlButton(session.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);

        return searchBlock;
    }
}
