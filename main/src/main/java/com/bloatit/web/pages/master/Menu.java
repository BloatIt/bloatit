package com.bloatit.web.pages.master;

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.url.DemandListPageUrl;
import com.bloatit.web.url.MembersListPageUrl;
import com.bloatit.web.url.ProjectListPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu_block");

        final HtmlDiv mainMenu = new HtmlDiv("main_menu", "main_menu");

        mainMenu.add(new HtmlDiv("menu_item").add(new DemandListPageUrl().getHtmlLink(Context.tr("Demands"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new ProjectListPageUrl().getHtmlLink(Context.tr("Projects"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Brainstorm"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new MembersListPageUrl().getHtmlLink(Context.tr("Members"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Documentation"))));

        add(mainMenu);
    }

}
