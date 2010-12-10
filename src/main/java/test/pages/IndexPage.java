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


import com.bloatit.framework.managers.DemandManager;
import com.bloatit.framework.managers.MemberManager;
import com.bloatit.web.utils.PageName;
import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlLink;
import test.html.components.standard.HtmlText;
import test.html.components.standard.form.HtmlButton;
import test.html.components.standard.form.HtmlForm;
import test.html.components.standard.form.HtmlTextField;
import test.pages.master.Page;

@PageName("index")
public class IndexPage extends Page {

    public IndexPage(final Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {

        HtmlDiv statsBlock = new HtmlDiv("index_stats_block");
        generateStatsBlock(statsBlock);

        HtmlDiv dualColumnBlock = new HtmlDiv("dual_column_block");
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

        HtmlDiv searchBlock = new HtmlDiv("index_search_block");

        HtmlForm searchForm = new HtmlForm(new UrlBuilder(GlobalSearchPage.class).buildUrl() , HtmlForm.Method.GET);

        HtmlTextField searchField = new HtmlTextField(GlobalSearchPage.SEARCH_CODE);

        HtmlButton searchButton = new HtmlButton(session.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);
        return searchBlock;
    }

    private void generateStatsBlock(HtmlDiv statsBlock) {
        statsBlock.add(new HtmlText("" + DemandManager.getDemandsCount() + " demands, " + MemberManager.getMembersCount()
                + " members..."));
    }

    private void generateDualColumnBlock(HtmlDiv dualColumnBlock) {
        
        HtmlDiv hightlightDemandsBlock = new HtmlDiv("index_hightlight_demands_block");
        generateHightlightDemandsBlock(hightlightDemandsBlock);

        dualColumnBlock.add(generateDescriptionBlock());
        dualColumnBlock.add(hightlightDemandsBlock);
    }

    private void generateHightlightDemandsBlock(HtmlDiv hightlightDemandsBlock) {

    }

    private HtmlDiv generateDescriptionBlock() {

        HtmlDiv descriptionBlock = new HtmlDiv("index_description_block");

        String description = session
                .tr("XXX is a platform to finance free software. Following, we must put a simple and complete description of the fonctionnement of XXXX.");
        descriptionBlock.add(new HtmlText(description));
        
        HtmlLink createIdeaPageLink = new HtmlLink(new UrlBuilder(CreateIdeaPage.class).buildUrl(), session.tr("Create a new idea"));
        descriptionBlock.add(createIdeaPageLink);

        return descriptionBlock;
    }
}
