//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.master;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.form.HtmlButton;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlForm.Method;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Image;
import com.bloatit.web.url.DocumentationRootPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu_block");

        final HtmlDiv mainMenu = new HtmlDiv("main_menu", "main_menu");

        final HtmlLink featureList = new FeatureListPageUrl().getHtmlLink(tr("Feature requests"));

//        final DocumentationPageUrl brainstormDocPage = new DocumentationPageUrl("brainstorm");
//        final HtmlLink brainstormList = brainstormDocPage.getHtmlLink(tr("Brainstorms"));
        
        final HtmlLink softwareList = new SoftwareListPageUrl().getHtmlLink(tr("Software"));
        
        final HtmlLink teamList = new TeamsPageUrl().getHtmlLink(tr("Teams"));
        final HtmlLink memberList = new MembersListPageUrl().getHtmlLink(tr("Members"));

        final HtmlSpan separator = new HtmlSpan("separator");

        separator.addText("–");

        final HtmlDiv featureAndBrainStormMenu = new HtmlDiv("menu_item");
        featureAndBrainStormMenu.add(featureList);
        featureAndBrainStormMenu.add(separator);
//        featureAndBrainStormMenu.add(brainstormList);
        featureAndBrainStormMenu.add(softwareList);
        mainMenu.add(featureAndBrainStormMenu);

        final HtmlDiv teamAndMemberMenu = new HtmlDiv("menu_item");
        teamAndMemberMenu.add(teamList);
        teamAndMemberMenu.add(separator);
        teamAndMemberMenu.add(memberList);
        mainMenu.add(teamAndMemberMenu);

        mainMenu.add(new HtmlDiv("menu_item").add(new DocumentationRootPageUrl().getHtmlLink(tr("Documentation"))));

        // Search form
        final FeatureListPageUrl formUrl = new FeatureListPageUrl();
        final HtmlForm searchForm = new HtmlForm(formUrl.urlString(), Method.GET);
        HtmlTextField searchField = new HtmlTextField(formUrl.getSearchStringParameter().getName());
        searchForm.add(searchField);
        searchField.addAttribute("placeholder", Context.tr("Search"));

        HtmlButton searchSubmit = new HtmlButton("Search");
        searchSubmit.addAttribute("type", "submit");
        searchSubmit.add(new HtmlImage(new Image("/resources/commons/img/magnifying_lense.png"), ""));

        searchForm.add(searchSubmit);

        mainMenu.add(new HtmlDiv("form_menu_item").add(searchForm));

        add(mainMenu);
    }

}
