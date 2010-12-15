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
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.custom.HtmlPagedList;
import com.bloatit.web.html.components.custom.HtmlProgressBar;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.idea.IdeaPage;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.IdeasListUrl;
import com.bloatit.web.utils.url.UrlBuilder;

@ParamContainer("ideas/list")
public class IdeasList extends Page {

    @PageComponent
    HtmlPagedList<Demand> pagedIdeaList;
    private final IdeasListUrl url;

    public IdeasList(final IdeasListUrl url) throws RedirectException {
        super();
        this.url = url;

        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Ideas list"), 1);

        final PageIterable<Demand> demandList = DemandManager.getDemands();

        final HtmlRenderer<Demand> demandItemRenderer = new IdeasListItem();

        final IdeasListUrl clonedUrl = url.clone();
        pagedIdeaList = new HtmlPagedList<Demand>(demandItemRenderer, demandList, clonedUrl, clonedUrl.getPagedIdeaListUrl());

        pageTitle.add(pagedIdeaList);

        add(pageTitle);
    }

    @Override
    public String getTitle() {
        return "View all ideas - search ideas";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    @Override
    protected String getCustomCss() {
        return "ideas-list.css";
    }

    static class IdeasListItem implements HtmlRenderer<Demand> {

        UrlBuilder demandPageUrlBuilder = new UrlBuilder(IdeaPage.class);
        private Demand demand;

        @Override
        public HtmlNode generate(final Demand idea) {
            this.demand = idea;
            demandPageUrlBuilder.addParameter(IdeaPage.IDEA_FIELD_NAME, idea);
            return new HtmlListItem(demandPageUrlBuilder.getHtmlLink(generateContent()));
        }

        private HtmlNode generateContent() {

            final HtmlDiv ideaBlock = new HtmlDiv();
            {
                final HtmlDiv leftBlock = new HtmlDiv();
                {
                    leftBlock.add(new HtmlText("VLC"));
                    leftBlock.add(new HtmlText("" + demand.getPopularity()));
                }
                ideaBlock.add(leftBlock);

                final HtmlDiv rightBlock = new HtmlDiv();
                {
                    final HtmlTitleBlock ideaTitle = new HtmlTitleBlock(demand.getTitle(), 3);
                    {
                        final HtmlProgressBar progressBar = new HtmlProgressBar(0.3f);
                        ideaTitle.add(progressBar);
                    }
                    rightBlock.add(ideaTitle);
                }
                ideaBlock.add(rightBlock);
            }
            return ideaBlock;
        }
    };
}
