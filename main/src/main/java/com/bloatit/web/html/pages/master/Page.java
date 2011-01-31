package com.bloatit.web.html.pages.master;

import java.io.IOException;
import java.util.Locale;

import com.bloatit.common.Image;
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
import com.bloatit.web.html.components.standard.HtmlImage;
import com.bloatit.web.html.pages.master.HtmlNotification.Level;
import com.bloatit.web.server.Context;
import com.bloatit.web.server.HttpResponse;
import com.bloatit.web.server.Linkable;
import com.bloatit.web.server.Notification;
import com.bloatit.web.server.Session;
import com.bloatit.web.utils.annotations.Messages;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.Url;

public abstract class Page extends HtmlElement implements Linkable {

    private final HtmlBranch content;
    private HtmlBranch notifications;
    private final HtmlDiv notificationBlock;
    private final Url thisUrl;
    protected final Session session;

    public Page(final Url url) {
        super();
        this.thisUrl = url;
        content = new HtmlDiv().setId("content");
        notifications = null;
        notificationBlock = new HtmlDiv("notifications");
        session = Context.getSession();
    }

    @Override
    public final void writeToHttp(final HttpResponse response) throws RedirectException, IOException {
        create();
        response.writePage(this);
    }

    public final void create() throws RedirectException {
        super.add(new HtmlTagText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        super.add(new HtmlTagText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"));
        final HtmlBranch html = new HtmlGenericElement("html");

        super.add(html);


        html.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        html.add(new Header(getTitle(), getCustomCss()));
        html.add(doGenerateBody());

        // Display waiting notifications
        addWaitingNotifications();

        // Set the last stable page into the session
        if (isStable()) {
            session.setTargetPage(null);
            session.setLastStablePage(thisUrl);
        }
        doCreate();
    }

    @SuppressWarnings("unused")
    protected void doCreate() throws RedirectException {
        // Nothing. You can override it or not.
    }

    private HtmlElement doGenerateBody() {

        final HtmlGenericElement body = new HtmlGenericElement("body");

        final HtmlBranch header = new HtmlDiv("header").setId("header");
        body.add(header);
        final HtmlBranch headerContent = new HtmlDiv("header_content").setId("header_content");
        header.add(headerContent);
        header.add(new HtmlGenericElement("hr").setId("header_end"));

        headerContent.add(generateLogo());
        headerContent.add(new SessionBar());



        body.add(new Menu());



        final HtmlBranch page = new HtmlDiv("page").setId("page");
        body.add(page);

        page.add(content);



        content.add(notificationBlock);

        body.add(new Footer());

        return body;
    }

    protected abstract String getTitle();

    public abstract boolean isStable();

    public final String getName() {
        if (getClass().getAnnotation(ParamContainer.class) != null) {
            return getClass().getAnnotation(ParamContainer.class).value();
        }
        return getClass().getName().toLowerCase(Locale.ENGLISH);
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
        if (notifications == null) {
            notifications = new HtmlDiv().setId("notifications");
            notificationBlock.add(notifications);
        }
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
            default:
                // do nothing
                break;
            }
        }
    }



    private HtmlElement generateLogo() {
        Context.getSession();

        HtmlDiv logoDiv = new HtmlDiv("logo", "logo");

        HtmlImage logoImage = new HtmlImage(new Image("logo_linkeos.png", Image.ImageType.LOCAL));
        logoImage.setCssClass("logo_linkeos");

        logoDiv.add(new IndexPageUrl().getHtmlLink(logoImage));

        return logoDiv;
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
            default:
                // do nothing, it should never append.
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
