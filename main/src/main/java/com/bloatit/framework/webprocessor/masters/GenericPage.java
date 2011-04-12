package com.bloatit.framework.webprocessor.masters;

import java.util.Locale;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.ErrorMessage;
import com.bloatit.framework.webprocessor.ErrorMessage.Level;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.XmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.context.Session;
import com.bloatit.framework.webprocessor.url.Messages;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.pages.master.HtmlNotification;

public abstract class GenericPage extends Page {

    private final Url thisUrl;
    private final Header pageHeader;
    protected final Session session;

    public GenericPage(final Url url) {
        super();
        this.thisUrl = url;
        session = Context.getSession();
        pageHeader = new Header(getTitle());

    }

    // -----------------------------------------------------------------------
    // Template method pattern: Abstract methods
    // -----------------------------------------------------------------------

    @Override
    public abstract boolean isStable();

    protected abstract void createPageContent(HtmlGenericElement body) throws RedirectException;

    @Override
    protected abstract String getTitle();

    protected abstract void addNotification(final HtmlNotification note);

    // -----------------------------------------------------------------------
    // Template method pattern: Implementation
    // -----------------------------------------------------------------------

    @Override
    public final void create() throws RedirectException {
        Log.framework().trace("Writing page: " + thisUrl.urlString());
        super.add(new XmlText("<!DOCTYPE html>"));
        
        final HtmlBranch html = new HtmlGenericElement("html");
        super.add(html);
        html.addAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        html.add(pageHeader);

        final HtmlGenericElement body = new HtmlGenericElement("body");
        html.add(body);
        
        // Abstract method cf: template method pattern
        createPageContent(body);

        // Set the last stable page into the session
        // Abstract method cf: template method pattern
        if (isStable()) {
            session.setTargetPage(null);
            session.setLastStablePage(thisUrl);
        }
        session.setLastVisitedPage(thisUrl);

        // Display waiting notifications
        // Abstract method cf: template method pattern
        addWaitingNotifications();
        
        // Do not forget to add the css/js files.
        generateDependencies();
    }

    /**
     * Generate dependancies to javascript and css files.
     * <p>
     * This method needs to be called before writing the page
     * </p>
     */
    private void generateDependencies() {
        for (final String css : getAllCss()) {
            pageHeader.addCss(css);
        }
        for (final String js : getAllJs()) {
            pageHeader.addJs(js);
        }
    }

    public final String getName() {
        if (getClass().getAnnotation(ParamContainer.class) != null) {
            return getClass().getAnnotation(ParamContainer.class).value();
        }
        return getClass().getName().toLowerCase(Locale.ENGLISH);
    }

    protected final void addNotifications(final Messages messages) {
        for (final Message message : messages) {
            session.notifyError(message.getMessage());
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
                    throw new BadProgrammerException("Unknown level: " + notification.getLevel());
            }
        }
        session.flushNotifications();
    }

}
