package com.bloatit.web.html.pages.master;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlBranch;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.HtmlNode;
import com.bloatit.web.html.HtmlTagText;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlLink;
import com.bloatit.web.html.pages.master.HtmlNotification.Level;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Notification;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.annotations.Messages;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Url;

public abstract class Page extends HtmlElement implements Linkable {

    private final HtmlBranch content;
    private final HtmlBranch notifications;
    protected final Session session;
    private final Url thisUrl;

    public Page(final Url url) {
        super();
        this.thisUrl = url;
        content = new HtmlDiv().setId("body_content");
        notifications = new HtmlDiv().setId("notifications");
        session = Context.getSession();
    }

    public void create() throws RedirectException {
        super.add(new HtmlTagText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        super.add(new HtmlTagText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"));
        final HtmlBranch html = new HtmlGenericElement("html");

        super.add(html);
        html.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        html.add(new Header(getTitle(), getCustomCss()));
        html.add(generate_body());

        // Display waiting notifications
        addWaitingNotifications();

        // Set the laste stable page into the session
        if (isStable()) {
            Context.getSession().setLastStablePage(thisUrl);
        }
    }

    // TODO correct empty div for notifications ?
    private HtmlElement generate_body() {

        final HtmlGenericElement body = new HtmlGenericElement("body");

        final HtmlBranch page = new HtmlDiv("page").setId("page");
        body.add(page);
        
        final HtmlBranch header = new HtmlDiv("header").setId("header");
        
        header.add(new TopBar());
        header.add(generateTitle());

        page.add(header);
        
        final HtmlBranch center = new HtmlDiv().setId("center");
        page.add(center);

        final HtmlBranch centerColumn = new HtmlDiv().setId("center_column");
        center.add(centerColumn);

        content.add(notifications);
        centerColumn.add(new Menu()).add(content);

        page.add(new Footer());

        return body;
    }

    protected abstract String getTitle();

    public abstract boolean isStable();

    public final String getName() {
        if (getClass().getAnnotation(ParamContainer.class) != null) {
            return getClass().getAnnotation(ParamContainer.class).value();
        } else {
            return getClass().getName().toLowerCase();
        }
    }

    protected String getCustomCss() {
        return null;
    }

    @Override
    public final HtmlElement addAttribute(final String name, final String value) {
        content.addAttribute(name, value);
        return this;
    }

    @Override
    public final HtmlElement add(final HtmlNode html) {
        content.add(html);
        return this;
    }

    @Override
    public final HtmlElement addText(final String text) {
        content.add(new HtmlText(text));
        return this;
    }

    protected final void setPageNotFound() {
        content.add(new HtmlDiv().setCssClass("not_found").addText(Context.tr("Page Not Found !")));
    }

    protected final void addNotification(final HtmlNotification note) {
        notifications.add(note);
    }

    protected final void addNotifications(final Messages messages) {
        for (final Message message : messages) {
            switch (message.getLevel()) {
            case INFO:
                addNotification(new HtmlNotification(Level.INFO, message.getMessage()));
                break;
            case WARNING:
                addNotification(new HtmlNotification(Level.WARNING, message.getMessage()));
                break;
            case ERROR:
                addNotification(new HtmlNotification(Level.ERROR, message.getMessage()));
                break;
            }
        }
    }

    protected final String generateLogo() {
        return "<span class=\"logo_bloatit\"><span class=\"logo_bloatit_bloat\">Bloat</span><span class=\"logo_bloatit_it\">It</span></span>";
    }

    private HtmlElement generateTitle() {
        Context.getSession();

        return new HtmlDiv().setId("logo").add(new HtmlLink(new IndexPageUrl().urlString(), new HtmlTagText(generateLogo())));
    }

    private void addWaitingNotifications() {

        for (final Notification notification : session.getNotifications()) {
            switch (notification.getType()) {
            case ERROR:
                addNotification(new HtmlNotification(Level.ERROR, notification.getMessage()));
                break;
            case BAD:
                addNotification(new HtmlNotification(Level.WARNING, notification.getMessage()));
                break;
            case GOOD:
                addNotification(new HtmlNotification(Level.INFO, notification.getMessage()));
                break;
            }
        }

        session.flushNotifications();
    }

    @Override
    public boolean selfClosable() {
        return false;
    }
}
