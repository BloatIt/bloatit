package test.pages.master;

import test.Context;
import test.HtmlElement;
import test.HtmlNode;
import test.HtmlText;
import test.Notification;
import test.Notification.Level;
import test.pages.components.HtmlBlock;

import com.bloatit.web.htmlrenderer.HtmlTools;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Message;
import com.bloatit.web.utils.PageName;
import com.bloatit.web.utils.RequestParamSetter.Messages;

public abstract class Page extends HtmlElement {

    private HtmlElement content;
    private HtmlElement notifications;

    public Page() {
        super("html");
        super.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        content = new HtmlBlock().setId("body_content");
        notifications = new HtmlBlock().setId("notifications");
    }

    public Page create() {
        super.add(new Header(getTitle(), getCustomCss()));
        super.add(generate_body());
        return this;
    }

    // TODO correct empty div for notifications ?
    private HtmlElement generate_body() {
        return new HtmlElement("body").add(new HtmlBlock()
                .setId("page")
                .add(new TopBar())
                .add(generateTitle())
                .add(new HtmlBlock().setId("center").add(new HtmlBlock().setId("center_column").add(new Menu())
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


    private HtmlElement generateTitle() {
        // TODO handle titles level !
        Session session = Context.getSession();
        return new HtmlElement("h1").addText(HtmlTools.generateLink(session, generateLogo(), new IndexPage(session)));
    }

}
