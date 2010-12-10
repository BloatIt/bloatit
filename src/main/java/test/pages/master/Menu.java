package test.pages.master;

import test.Context;
import test.UrlBuilder;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlLink;
import test.html.components.standard.HtmlList;
import test.html.components.standard.HtmlListItem;
import test.pages.DemandsPage;
import test.pages.IndexPage;
import test.pages.MembersListPage;
import test.pages.PageNotFound;
import test.pages.SpecialsPage;

import com.bloatit.web.server.Session;

public class Menu extends HtmlDiv {

    protected Menu() {
        super();

        final Session s = Context.getSession();
        setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(DemandsPage.class).buildUrl(), s.tr("Demands"))));
        primaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), s.tr("Projects"))));
        primaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), s.tr("Groups"))));
        primaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(MembersListPage.class).buildUrl(), s.tr("Members"))));

        final HtmlList secondaryList = new HtmlList();

        secondaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(SpecialsPage.class).buildUrl(), s.tr("Specials page"))));
        secondaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Contact"))));
        secondaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Documentation"))));
        secondaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("About BloatIt"))));
        secondaryList.addItem(new HtmlListItem(new HtmlLink(new UrlBuilder(PageNotFound.class).buildUrl(), s.tr("Press"))));

        add(primaryList);
        add(secondaryList);

    }

}
