package com.bloatit.web.html.pages.master;

import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.DemandListUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.PageNotFoundUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu_block");

        final HtmlDiv mainMenu = new HtmlDiv("main_menu", "main_menu");



        mainMenu.add(new HtmlDiv("menu_item").add(new DemandListUrl().getHtmlLink(Context.tr("Demands"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Projects"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Brainstorm"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new MembersListPageUrl().getHtmlLink(Context.tr("Members"))));
        mainMenu.add(new HtmlDiv("menu_item").add(new PageNotFoundUrl().getHtmlLink(Context.tr("Documentation"))));

        add(mainMenu);
    }

}
