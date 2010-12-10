package test;

import test.html.HtmlNode;
import test.html.HtmlText;
import test.html.HtmlElement;
import test.Notification.Level;
import test.html.components.basicComponents.HtmlBlock;
import test.html.components.basicComponents.HtmlList;
import test.html.components.basicComponents.HtmlListItem;

import com.bloatit.web.actions.LogoutAction;
import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.DemandsPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.LoginPage;
import com.bloatit.web.pages.MembersListPage;
import com.bloatit.web.pages.MyAccountPage;
import com.bloatit.web.pages.PageNotFound;
import com.bloatit.web.pages.SpecialsPage;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Message;
import com.bloatit.web.utils.RequestParamSetter.Messages;
import test.html.components.basicComponents.HtmlGenericElement;
import test.pages.HtmlContainerElement;

public abstract class Page extends HtmlElement {

    private static final String DESIGN = "/resources/css/core.css";
    private HtmlContainerElement content;
    private HtmlContainerElement notifications;

    public Page() {
        super("html");
        super.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        content = new HtmlBlock().setId("body_content");
        notifications = new HtmlBlock().setId("notifications");
    }

    public Page create() {
        HtmlContainerElement head = new HtmlGenericElement("head");
        HtmlContainerElement meta = new HtmlGenericElement("meta")
                .addAttribute("http-equiv", "content-type")
                .addAttribute("content", "text/html;charset=utf-8");

        HtmlContainerElement link = new HtmlGenericElement("link").addAttribute("rel", "stylesheet")
                                                  .addAttribute("href", DESIGN)
                                                  .addAttribute("type", "text/css")
                                                  .addAttribute("media", "handheld, all");

        head.add(meta).add(link);

        String customCss;
        if ((customCss = getCustomCss()) != null) {
            HtmlContainerElement customlink = new HtmlGenericElement("link").addAttribute("rel", "stylesheet")
                                                            .addAttribute("href", "/resources/css/" + customCss)
                                                            .addAttribute("type", "text/css")
                                                            .addAttribute("media", "handheld, all");
            head.add(customlink);
        }

        head.add(new HtmlGenericElement("title").addText("Bloatit - " + getTitle()));

        super.add(head);

        super.add(generate_body());
        return this;
    }

    // TODO correct empty div for notifications ?
    private HtmlElement generate_body() {
        return new HtmlGenericElement("body").add(new HtmlBlock().setId("page")
                                                          .add(generateTopBar())
                                                          .add(generateTitle())
                                                          .add(new HtmlBlock().setId("center").add(new HtmlBlock().setId("center_column")
                                                                                                                  .add(generateMainMenu())
                                                                                                                  .add(content.add(notifications))))
                                                          .add(generateFooter()));
    }

    protected abstract String getTitle();

    public abstract boolean isStable();

    protected String getCustomCss() {
        return null;
    }

    public HtmlElement addAttribute(String name, String value) {
        content.addAttribute(name, value);
        return this;
    }

    public HtmlElement add(HtmlNode html) {
        content.add(html);
        return this;
    }

    public HtmlElement addText(String text) {
        content.add(new HtmlText(text));
        return this;
    }

    protected void setPageNotFound() {
        // TODO translate
        content.add(new HtmlBlock().setClass("not_found").addText("Page Not Found !"));
    }

    protected void addNotification(Notification note) {
        notifications.add(note);
    }

    protected void addNotifications(Messages messages) {
        for (Message message : messages) {
            switch (message.getLevel()) {
            case INFO:
                addNotification(new Notification(Level.INFO, message.getMessage()));
                break;
            case WARNING:
                addNotification(new Notification(Level.WARNING, message.getMessage()));
                break;
            case ERROR:
                addNotification(new Notification(Level.ERROR, message.getMessage()));
                break;
            }
        }
    }

    protected String generateLogo() {
        return "<span class=\"logo_bloatit\"><span class=\"logo_bloatit_bloat\">Bloat</span><span class=\"logo_bloatit_it\">It</span></span>";
    }

    private HtmlElement generateTopBar() {
        HtmlContainerElement topBar = new HtmlBlock().setId("top_bar");

        Session session = Context.getSession();
        if (session.isLogged()) {
            final String full_name = session.getAuthToken().getMember().getFullname();
            final String karma = HtmlTools.compressKarma(session.getAuthToken().getMember().getKarma());
            final String memberLink = HtmlTools.generateLink(session, full_name, new MyAccountPage(session)) + "<span class=\"karma\">" + karma + "</span>";
            final String logoutLink = HtmlTools.generateLink(session, session.tr("Logout"), new LogoutAction(session));

            topBar.add(new HtmlGenericElement("span").setClass("top_bar_component").addText(memberLink));
            topBar.add(new HtmlGenericElement("span").setClass("top_bar_component").addText(logoutLink));

        } else {
            topBar.add(new HtmlGenericElement("span").setClass("top_bar_component").addText(HtmlTools.generateLink(session,
                                                                                                            session.tr("Login / Signup"),
                                                                                                            new LoginPage(session))));

        }
        return topBar;
    }

    private HtmlElement generateMainMenu() {

        final Session s = Context.getSession();
        final HtmlContainerElement mainMenu = new HtmlBlock().setId("main_menu");

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

        mainMenu.add(primaryList);
        mainMenu.add(secondaryList);

        return mainMenu;
    }

    private HtmlElement generateTitle() {
        // TODO handle titles level !
        Session session = Context.getSession();
        return new HtmlGenericElement("h1").addText(HtmlTools.generateLink(session, generateLogo(), new IndexPage(session)));
    }

    private HtmlElement generateFooter() {
        return new HtmlBlock().setId("footer").addText(Context.tr("This website is under GNU Affero Public Licence."));
    }

}
