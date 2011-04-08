package com.bloatit.web.pages.master;

import com.bloatit.framework.webprocessor.Context;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.web.url.DocumentationPageUrl;
import com.bloatit.web.url.FeatureListPageUrl;
import com.bloatit.web.url.SoftwareListPageUrl;
import com.bloatit.web.url.TeamsPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu_block");

        final HtmlDiv mainMenu = new HtmlDiv("main_menu", "main_menu");

        mainMenu.add(new HtmlDiv("menu_item").add(new FeatureListPageUrl().getHtmlLink(Context.tr("Features"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new SoftwareListPageUrl().getHtmlLink(Context.tr("Softwares"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Brainstorm"))));
        // mainMenu.add(new HtmlDiv("menu_item").add(new
        // MembersListPageUrl().getHtmlLink(Context.tr("Members"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new TeamsPageUrl().getHtmlLink(Context.tr("Teams"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new DocumentationPageUrl().getHtmlLink(Context.tr("Documentation"))));

        add(mainMenu);
    }

}
