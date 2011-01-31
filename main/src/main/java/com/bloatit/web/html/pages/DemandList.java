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
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
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

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(Context.tr("Demand list"), 1);

        final PageIterable<Demand> ideaList = DemandManager.getDemands();

        final HtmlRenderer<Demand> demandItemRenderer = new IdeasListItem();

        final DemandListUrl clonedUrl = url.clone();
        pagedDemandList = new HtmlPagedList<Demand>(demandItemRenderer, ideaList, clonedUrl, clonedUrl.getPagedDemandListUrl());

        pageTitle.add(pagedDemandList);

        add(pageTitle);
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
