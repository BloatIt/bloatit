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

//import java.util.Random;
import com.bloatit.common.Image;
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
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.HtmlRenderer;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.IdeasListUrl;

@ParamContainer("ideas/list")
public class IdeasList extends Page {

    @PageComponent
    HtmlPagedList<Demand> pagedIdeaList;
    private final IdeasListUrl url;

    public IdeasList(final IdeasListUrl url) throws RedirectException {
        super(url);
        this.url = url;

        generateContent();
    }

    private void generateContent() {

        final HtmlTitleBlock pageTitle = new HtmlTitleBlock(session.tr("Ideas list"), 1);

        final PageIterable<Demand> ideaList = DemandManager.getDemands();

        final HtmlRenderer<Demand> demandItemRenderer = new IdeasListItem();

        final IdeasListUrl clonedUrl = url.clone();
        pagedIdeaList = new HtmlPagedList<Demand>(demandItemRenderer, ideaList, clonedUrl, clonedUrl.getPagedIdeaListUrl());

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

        private Demand idea;

        @Override
        public HtmlNode generate(final Demand idea) {
            this.idea = idea;

            return generateContent();
        }

        private HtmlNode generateContent() {

            final HtmlDiv ideaBlock = new HtmlDiv("idea_summary");
            {






                final HtmlDiv leftBlock = new HtmlDiv("idea_summary_left");
                {

                    final HtmlDiv karmaBlock = new HtmlDiv("idea_karma");
                    karmaBlock.add(new HtmlParagraph("" + idea.getPopularity()));

                    leftBlock.add(karmaBlock);

                }
                //ideaLinkBlock.add(leftBlock);
                ideaBlock.add(leftBlock);

                final HtmlDiv centerBlock = new HtmlDiv("idea_summary_center");
                {

                	
                	
                	
                    final HtmlLink linkTitle = new IdeaPageUrl(idea).getHtmlLink("Correction de bug - VLC");
                    linkTitle.setCssClass("idea_link");



                    final HtmlTitleBlock ideaTitle = new HtmlTitleBlock(linkTitle, 3);
                    {

                        final HtmlLink linkText = new IdeaPageUrl(idea).getHtmlLink(new HtmlParagraph(idea.getTitle()));
                        linkText.setCssClass("idea_link_text");

                        ideaTitle.add(linkText);

                        float progressValue = (float) Math.floor(idea.getProgression());

                        if (progressValue > 100) {
                            progressValue = 100;
                        }

                        final HtmlProgressBar progressBar = new HtmlProgressBar(progressValue);
                        final HtmlParagraph progressText = new HtmlParagraph("<span class=important>5684 €</span> soit <span class=important>45%</span> de <span class=important>10000 €</span> demandés", "idea_progress_text");
                        
                        ideaTitle.add(progressBar);
                        ideaTitle.add(progressText);
                    }
                    centerBlock.add(ideaTitle);

                }
                //ideaLinkBlock.add(centerBlock);
                ideaBlock.add(centerBlock);

                final HtmlDiv rightBlock = new HtmlDiv("idea_summary_right");
                {
                    rightBlock.add(new HtmlImage(new Image("/resources/img/idea.png", Image.ImageType.DISTANT)));
                }
                //ideaLinkBlock.add(rightBlock);
                ideaBlock.add(rightBlock);
            }
            return ideaBlock;
        }
    };
}
