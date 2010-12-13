package com.bloatit.web.html.pages.master;


import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.pages.IndexPage;
import com.bloatit.web.html.pages.master.Notification.Level;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.Message;
import com.bloatit.web.utils.annotations.PageName;
import com.bloatit.web.utils.annotations.RequestParamSetter.Messages;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public abstract class Page extends HtmlElement implements Linkable {

    private final HtmlBranch content;
    private final HtmlBranch notifications;

    protected final Request request;
    protected final Session session;

    public Page(final Request request) {
        super("html");
        session = Context.getSession();

        this.request = request;
        super.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        content = new HtmlDiv().setId("body_content");
        notifications = new HtmlDiv().setId("notifications");
    }

    public void create() throws RedirectException {
        super.add(new Header(getTitle(), getCustomCss()));
        super.add(generate_body());
    }

    // TODO correct empty div for notifications ?
    private HtmlElement generate_body() {
        return new HtmlGenericElement("body").add(new HtmlDiv().setId("page").add(new TopBar()).add(generateTitle())
                .add(new HtmlDiv().setId("center").add(new HtmlDiv().setId("center_column").add(new Menu()).add(content.add(notifications))))
                .add(new Footer()));
    }

    protected abstract String getTitle();

    public abstract boolean isStable();

    public String getName() {
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
    public HtmlElement addAttribute(final String name, final String value) {
        content.addAttribute(name, value);
        return this;
    }

    @Override
    public HtmlElement add(final HtmlNode html) {
        content.add(html);
        return this;
    }

    @Override
    public HtmlElement addText(final String text) {
        content.add(new HtmlText(text));
        return this;
    }

    protected void setPageNotFound() {
        // TODO translate
        content.add(new HtmlDiv().setCssClass("not_found").addText("Page Not Found !"));
    }

    protected void addNotification(final Notification note) {
        notifications.add(note);
    }

    protected void addNotifications(final Messages messages) {
        for (final Message message : messages) {
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
        Context.getSession();

        return new HtmlGenericElement("h1").add(new HtmlLink(new UrlBuilder(IndexPage.class).buildUrl(), generateLogo()));
    }

}
