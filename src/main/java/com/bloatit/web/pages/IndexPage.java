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

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.framework.managers.MemberManager;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlBlock;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class IndexPage extends Page {

    public IndexPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public IndexPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    protected HtmlComponent generateContent() {

        HtmlContainer allPage = new HtmlContainer();

        HtmlBlock searchBlock = new HtmlBlock("index_search_block");
        generateSearchBlock(searchBlock);

        HtmlBlock statsBlock = new HtmlBlock("index_stats_block");
        generateStatsBlock(searchBlock);

        HtmlBlock dualColumnBlock = new HtmlBlock("dual_column_block");
        generateDualColumnBlock(searchBlock);



        allPage.add(searchBlock);
        allPage.add(statsBlock);
        allPage.add(dualColumnBlock);

        return allPage;

    }

    @Override
    public String getCode() {
        return "index";
    }

    @Override
    protected String getTitle() {
        return "Finance free software";
    }

    @Override
    public boolean isStable() {
        return true;
    }

    private void generateSearchBlock(HtmlBlock searchBlock) {
        GlobalSearchPage globalSearchPage = new GlobalSearchPage(session);
        HtmlForm searchForm = new HtmlForm(globalSearchPage);
        searchForm.setMethod(HtmlForm.Method.GET);
        HtmlTextField searchField = new HtmlTextField();
        searchField.setName(globalSearchPage.getSearchCode());

        HtmlButton searchButton = new HtmlButton(session.tr("Search"));
        searchForm.add(searchField);
        searchForm.add(searchButton);
        searchBlock.add(searchForm);
    }

    private void generateStatsBlock(HtmlBlock statsBlock) {
        statsBlock.add(new HtmlText(""+DemandManager.getDemandsCount()+ " demands, "+MemberManager.getMembersCount()+" members..."));
    }

    private void generateDualColumnBlock(HtmlBlock dualColumnBlock) {
        HtmlBlock descriptionBlock = new HtmlBlock("index_description_block");
        generateDescriptionBlock(descriptionBlock);

        HtmlBlock hightlightDemandsBlock = new HtmlBlock("index_hightlight_demands_block");
        generateHightlightDemandsBlock(hightlightDemandsBlock);


        dualColumnBlock.add(descriptionBlock);
        dualColumnBlock.add(hightlightDemandsBlock);
    }

    private void generateHightlightDemandsBlock(HtmlBlock hightlightDemandsBlock) {
        
    }

    private void generateDescriptionBlock(HtmlBlock descriptionBlock) {
        String description = session.tr("XXX is a platform to finance free software. Following, we must put a simple and complete description of the fonctionnement of XXXX.");
        descriptionBlock.add(new HtmlText(description));
    }
}
