package com.bloatit.web.pages.master;

import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.url.CreateFeaturePageUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu_block");

        final HtmlDiv mainMenu = new HtmlDiv("main_menu", "main_menu");

        final HtmlLink featureList = new FeatureListPageUrl().getHtmlLink(Context.tr("Feature requests"));

        new CreateFeaturePageUrl().getHtmlLink(Context.tr("New request"));

        final HtmlLink softwareList = new SoftwareListPageUrl().getHtmlLink(Context.tr("Softwares"));
        final DocumentationPageUrl brainstormDocPage = new DocumentationPageUrl();
        brainstormDocPage.setDocTarget("brainstorm");
        final HtmlLink brainstormList = brainstormDocPage.getHtmlLink(Context.tr("Brainstorms"));
        final HtmlLink teamList = new TeamsPageUrl().getHtmlLink(Context.tr("Teams"));
        final HtmlLink memberList = new MembersListPageUrl().getHtmlLink(Context.tr("Members"));

        final HtmlSpan separator = new HtmlSpan("separator");

        separator.addText("–");

        final HtmlDiv featureAndBrainStormMenu = new HtmlDiv("menu_item");
        featureAndBrainStormMenu.add(featureList);
        featureAndBrainStormMenu.add(separator);
        featureAndBrainStormMenu.add(brainstormList);
        mainMenu.add(featureAndBrainStormMenu);

        final HtmlDiv teamAndMemberMenu = new HtmlDiv("menu_item");
        teamAndMemberMenu.add(teamList);
        teamAndMemberMenu.add(separator);
        teamAndMemberMenu.add(memberList);
        mainMenu.add(teamAndMemberMenu);

        mainMenu.add(new HtmlDiv("menu_item").add(softwareList));
        mainMenu.add(new HtmlDiv("menu_item").add(new DocumentationPageUrl().getHtmlLink(Context.tr("About"))));

        add(mainMenu);
    }

}
