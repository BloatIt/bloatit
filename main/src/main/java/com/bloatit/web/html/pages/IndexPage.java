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

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlParagraph;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;
import com.bloatit.web.utils.url.GlobalSearchPageUrl;
import com.bloatit.web.utils.url.IndexPageUrl;

@ParamContainer("index")
public final class IndexPage extends Page {

    public IndexPage(final IndexPageUrl indexPageUrl) {
        super(indexPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {
        final HtmlDiv statsBlock = new HtmlDiv("index_stats_block");
        generateStatsBlock(statsBlock);

        final HtmlDiv dualColumnBlock = new HtmlDiv("dual_column_block");
        generateDualColumnBlock(dualColumnBlock);

        add(generateSearchBlock());
        add(statsBlock);
        add(dualColumnBlock);

    }

    @Override
    protected String getTitle() {
        return "Finance free software";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private HtmlDiv generateSearchBlock() {

        final HtmlDiv searchBlock = new HtmlDiv("index_search_block");

        final HtmlForm searchForm = new HtmlForm(new GlobalSearchPageUrl().urlString(), HtmlForm.Method.GET);

        final HtmlTextField searchField = new HtmlTextField(GlobalSearchPage.SEARCH_CODE);

        final HtmlSubmit searchButton = new HtmlSubmit(Context.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);
        return searchBlock;
    }

    private void generateStatsBlock(final HtmlDiv statsBlock) {
        statsBlock.add(new HtmlParagraph("" + DemandManager.getDemandsCount() + " demands, " + MemberManager.getMembersCount() + " members..."));
    }

    private void generateDualColumnBlock(final HtmlDiv dualColumnBlock) {

        final HtmlDiv hightlightDemandsBlock = new HtmlDiv("index_hightlight_demands_block");

        dualColumnBlock.add(generateDescriptionBlock());
        dualColumnBlock.add(hightlightDemandsBlock);
    }

    private HtmlDiv generateDescriptionBlock() {

        final HtmlDiv descriptionBlock = new HtmlDiv("index_description_block");

        final String description = Context
                .tr("XXX is a platform to finance free software. Following, we must put a simple and complete description of the fonctionnement of XXXX.");
        descriptionBlock.add(new HtmlParagraph(description));

        final HtmlLink createIdeaPageLink = new HtmlLink(new CreateIdeaPageUrl().urlString(), Context.tr("Submit a new idea"));
        descriptionBlock.add(createIdeaPageLink);

        return descriptionBlock;
    }
}
