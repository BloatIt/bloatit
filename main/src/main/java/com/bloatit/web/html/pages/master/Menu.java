package com.bloatit.web.html.pages.master;


import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.components.standard.HtmlList;
import com.bloatit.web.html.components.standard.HtmlListItem;
import com.bloatit.web.html.pages.IdeasList;
import com.bloatit.web.html.pages.IndexPage;
import com.bloatit.web.html.pages.MembersListPage;
import com.bloatit.web.html.pages.PageNotFound;
import com.bloatit.web.html.pages.SpecialsPage;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.url.UrlBuilder;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        final Session s = Context.getSession();
        setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.add(new HtmlLink(new UrlBuilder(IdeasList.class).buildUrl(), s.tr("Ideas")));
        primaryList.add(new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), s.tr("Projects")));
        primaryList.add((new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), s.tr("Groups"))));
        primaryList.add(new HtmlLink(new UrlBuilder(MembersListPage.class).buildUrl(), s.tr("Members")));

        final HtmlList secondaryList = new HtmlList();

        secondaryList.add(new HtmlLink(new UrlBuilder(SpecialsPage.class).buildUrl(), s.tr("Specials page")));
        secondaryList.add(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Contact")));
        secondaryList.add(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Documentation")));
        secondaryList.add(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("About BloatIt")));
        secondaryList.add(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Press")));

        add(primaryList);
        add(secondaryList);

    }

}
