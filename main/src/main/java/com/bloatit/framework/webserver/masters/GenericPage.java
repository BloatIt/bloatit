package com.bloatit.framework.webserver.masters;

import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.ErrorMessage;
import com.bloatit.framework.webserver.ErrorMessage.Level;
import com.bloatit.framework.webserver.Session;
import com.bloatit.framework.webserver.annotations.Message;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlTagText;
import com.bloatit.framework.webserver.url.Messages;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.pages.master.HtmlNotification;

public abstract class GenericPage extends Page {

    protected final Url thisUrl;
    protected final Session session;

    public GenericPage(final Url url) {
        super();
        this.thisUrl = url;
        session = Context.getSession();

    }

    @Override
    public final void create() throws RedirectException {
        Log.framework().trace("Writing page: " + thisUrl.urlString());
        super.add(new HtmlTagText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        super.add(new HtmlTagText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">"));
        final HtmlBranch html = new HtmlGenericElement("html");

        super.add(html);

        html.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");

        html.add(new Header(getTitle(), getCustomCss()));

        final HtmlGenericElement body = new HtmlGenericElement("body");
        html.add(body);

        generateBody(body);

        // Set the last stable page into the session
        if (isStable()) {
            session.setTargetPage(null);
            session.setLastStablePage(thisUrl);
        }
        session.setLastVisitedPage(thisUrl);

        // Display waiting notifications
        addWaitingNotifications();
    }

    protected abstract void generateBody(HtmlGenericElement body) throws RedirectException;

    @Override
    protected abstract String getTitle();

    @Override
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

    protected abstract void addNotification(final HtmlNotification note);

    protected final void addNotifications(final Messages messages) {
        for (final Message message : messages) {
            addNotification(new HtmlNotification(Level.FATAL, message.getMessage()));
        }
    }

    private void addWaitingNotifications() {

        for (final ErrorMessage notification : session.getNotifications()) {
            switch (notification.getLevel()) {
                case FATAL:
                    addNotification(new HtmlNotification(Level.FATAL, notification.getMessage()));
                    break;
                case WARNING:
                    addNotification(new HtmlNotification(Level.WARNING, notification.getMessage()));
                    break;
                case INFO:
                    addNotification(new HtmlNotification(Level.INFO, notification.getMessage()));
                    break;
                default:
                    // do nothing, it should never append.
                    break;
            }
        }

        session.flushNotifications();
    }
}
