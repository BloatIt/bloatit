package com.bloatit.web.html.pages.master;

import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.IdeasListUrl;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.PageNotFoundUrl;
import com.bloatit.web.utils.url.SpecialsPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.add(new HtmlLink(new IdeasListUrl().urlString(), Context.tr("Ideas")));
        primaryList.add(new HtmlLink(new IndexPageUrl().urlString(), Context.tr("Projects")));
        primaryList.add((new HtmlLink(new IndexPageUrl().urlString(), Context.tr("Groups"))));
        primaryList.add(new HtmlLink(new MembersListPageUrl().urlString(), Context.tr("Members")));

        final HtmlList secondaryList = new HtmlList();

        secondaryList.add(new HtmlLink(new SpecialsPageUrl().urlString(), Context.tr("Specials page")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), Context.tr("Contact")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), Context.tr("Documentation")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), Context.tr("About BloatIt")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), Context.tr("Press")));

        add(primaryList);
        add(secondaryList);

    }

}
