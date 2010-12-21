package com.bloatit.web.html.pages.master;

import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.IdeasListUrl;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.MembersListPageUrl;
import com.bloatit.web.utils.url.PageNotFoundUrl;
import com.bloatit.web.utils.url.SpecialsPageUrl;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        final Session s = Context.getSession();
        setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.add(new HtmlLink(new IdeasListUrl().urlString(), s.tr("Ideas")));
        primaryList.add(new HtmlLink(new IndexPageUrl().urlString(), s.tr("Projects")));
        primaryList.add((new HtmlLink(new IndexPageUrl().urlString(), s.tr("Groups"))));
        primaryList.add(new HtmlLink(new MembersListPageUrl().urlString(), s.tr("Members")));

        final HtmlList secondaryList = new HtmlList();

        secondaryList.add(new HtmlLink(new SpecialsPageUrl().urlString(), s.tr("Specials page")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), s.tr("Contact")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), s.tr("Documentation")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), s.tr("About BloatIt")));
        secondaryList.add(new HtmlLink(new PageNotFoundUrl().urlString(), s.tr("Press")));

        add(primaryList);
        add(secondaryList);

    }

}
