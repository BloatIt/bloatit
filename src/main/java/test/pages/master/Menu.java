package test.pages.master;

import test.Context;
import test.pages.components.HtmlBlock;
import test.pages.components.HtmlList;
import test.pages.components.HtmlListItem;

import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.SpecialsPage;
import com.bloatit.web.server.Session;

public class Menu extends HtmlBlock {

    protected Menu() {
        super();

        final Session s = Context.getSession();
        setId("main_menu");

        final HtmlList primaryList = new HtmlList();

        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Demands"), new DemandsPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Projects"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Groups"), new IndexPage(s))));
        primaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Members"), new MembersListPage(s))));

        final HtmlList secondaryList = new HtmlList();

        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Specials page"), new SpecialsPage(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Contact"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Documentation"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("About BloatIt"), new PageNotFound(s))));
        secondaryList.addItem(new HtmlListItem(HtmlTools.generateLink(s, s.tr("Press"), new PageNotFound(s))));

        add(primaryList);
        add(secondaryList);

    }

}
