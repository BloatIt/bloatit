package test.pages.master;

import test.Context;
import test.Notification;
import test.Notification.Level;

import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Message;
import com.bloatit.web.utils.PageName;
import com.bloatit.web.utils.RequestParamSetter.Messages;
import test.Linkable;
import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.html.HtmlElement;
import test.html.HtmlNode;
import test.html.HtmlText;
import test.html.components.standard.HtmlDiv;
import test.html.components.standard.HtmlGenericElement;
import test.html.components.standard.HtmlLink;
import test.pages.HtmlContainerElement;
import test.pages.IndexPage;

public abstract class Page extends HtmlElement implements Linkable {

    private HtmlContainerElement content;
    private HtmlContainerElement notifications;
    
    protected final Request request;
    protected final Session session;

    public Page(Request request) {
        super("html");
        session = Context.getSession();

        this.request = request;
        super.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        content = new HtmlDiv().setId("body_content");
        notifications = new HtmlDiv().setId("notifications");
    }

    @Override
    public String process() throws RedirectException {
        super.add(new Header(getTitle(), getCustomCss()));
        super.add(generate_body());
        return null;
    }

    // TODO correct empty div for notifications ?
    private HtmlElement generate_body() {
        return new HtmlGenericElement("body").add(new HtmlDiv()
                .setId("page")
                .add(new TopBar())
                .add(generateTitle())
                .add(new HtmlDiv().setId("center").add(new HtmlDiv().setId("center_column").add(new Menu())
                        .add(content.add(notifications)))).add(new Footer()));
    }

    protected abstract String getTitle();

    public abstract boolean isStable();
    

    public String getName(){
        if (getClass().getAnnotation(PageName.class) != null) {
            return getClass().getAnnotation(PageName.class).value();
        } else {
            return getClass().getName().toLowerCase();
        }
    }

    protected String getCustomCss() {
        return null;
    }

    @Override
    public HtmlElement addAttribute(String name, String value) {
        content.addAttribute(name, value);
        return this;
    }

    @Override
    public HtmlElement add(HtmlNode html) {
        content.add(html);
        return this;
    }

    @Override
    public HtmlElement addText(String text) {
        content.add(new HtmlText(text));
        return this;
    }

    protected void setPageNotFound() {
        // TODO translate
        content.add(new HtmlDiv().setCssClass("not_found").addText("Page Not Found !"));
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


    private HtmlElement generateTitle() {
        // TODO handle titles level !
        Session session = Context.getSession();

        return new HtmlGenericElement("h1").add(new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), generateLogo()));
    }

}
