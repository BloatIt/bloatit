//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.framework.webprocessor.masters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.model.ModelAccessor;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.datetime.TimeRenderer;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.ErrorMessage;
import com.bloatit.framework.webprocessor.WebProcessor;
import com.bloatit.framework.webprocessor.annotations.DefaultTranslator;
import com.bloatit.framework.webprocessor.annotations.Message;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlNode;
import com.bloatit.framework.webprocessor.components.meta.HtmlNonEscapedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Header.Robot;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.xcgiserver.HttpReponseField.StatusCode;
import com.bloatit.framework.xcgiserver.HttpResponse;
import com.bloatit.web.linkable.master.HtmlNotification;

public abstract class Page implements Linkable {

    private static final int SECONDS_BEFORE_SHOWING_MESSAGE_PREFIX = 120;
    private final Url thisUrl;
    private Header pageHeader;

    public Page(final Url url) {
        super();
        ModelAccessor.setReadOnly();
        this.thisUrl = url;
    }

    @Override
    public final void writeToHttp(final HttpResponse response, final WebProcessor server) throws RedirectException, IOException {
        // The create is done before the getContentType and getResponseStatus.
        HtmlElement page = create();
        response.writePage(getResponseStatus(), getContentType(), page);
    }

    public final String getName() {
        if (getClass().getAnnotation(ParamContainer.class) != null) {
            return getClass().getAnnotation(ParamContainer.class).value();
        }
        return getClass().getName().toLowerCase(Locale.ENGLISH);
    }

    public final Url getUrl() {
        return thisUrl;
    }

    // -----------------------------------------------------------------------
    // Template method pattern: Abstract methods
    // -----------------------------------------------------------------------

    public abstract boolean isStable();

    /**
     * Indicates the status of the page
     *
     * @return a status code matching the real status of the response
     */
    protected abstract StatusCode getResponseStatus();

    /**
     * Get the content type of this page. We are looking for "text/html", but it
     * can also be "application/json" or "text/xml". Overload me if you want to
     * change this content type.
     *
     * @return "text/html".
     */
    protected String getContentType() {
        return "text/html";
    }

    /**
     * This method is called only if {@link #createBody()} and
     * {@link #createBodyOnParameterError()} does not throw exception.
     *
     * @return the title of the page.
     */
    protected abstract String getTitle();

    /**
     * Create the body tag of the HTML page. This method is called when there
     * <b>is no error</b> in the parameters from the {@link Url}.
     *
     * @return the body tag of the HTML page.
     * @throws RedirectException if there is a problem during the page
     *             construction and we want to redirect on an other page.
     */
    protected abstract HtmlGenericElement createBody() throws RedirectException;

    /**
     * Create the body tag of the HTML page. This method is called when there
     * <b>is at least one error</b> in the parameters from the {@link Url}.
     *
     * @return the body tag of the HTML page.
     * @throws RedirectException if there is a problem during the page
     *             construction and we want to redirect on an other page.
     */
    protected abstract HtmlGenericElement createBodyOnParameterError() throws RedirectException;

    /**
     * Adds a user notification
     */
    protected abstract void addNotification(final HtmlNotification note);

    /**
     * A method that returns the description of the page as inserted inside the
     * {@code <meta name="description">} tag in page header.
     *
     * @return the string describing the page
     */
    protected abstract String getPageDescription();

    /**
     * A method that returns the list of the keywords inserted inside the
     * {@code <meta name="keywords">} tag in page header.
     *
     * @return the string describing the site
     */
    protected abstract String getPageKeyWords();

    /**
     * A method that returns the list of page specific robots information
     * inserted inside the {@code <meta name="robots">} tag in page header.
     *
     * @return the list of keywords
     */
    protected abstract Set<Robot> getRobots();

    protected abstract ArrayList<HtmlHeaderLink> getLinks();

    protected abstract ArrayList<HtmlElement> getMetas();

    // -----------------------------------------------------------------------
    // Template method pattern: procedure.
    // -----------------------------------------------------------------------

    private final HtmlElement create() throws RedirectException {
        Log.framework().trace("Writing page: " + thisUrl.urlString());
        final PlaceHolderElement page = new PlaceHolderElement();

        HtmlGenericElement bodyContent;
        if (thisUrl.hasError()) {
            for (final Message message : thisUrl.getMessages()) {
                Context.getSession().notifyError(Context.tr(message.getMessage(Context.getLocalizator())));
                Log.framework().trace("Error messages from Url system: " + message.getMessage(new DefaultTranslator()));
            }
            // Abstract method cf: template method pattern
            bodyContent = createBodyOnParameterError();
        } else {
            // Abstract method cf: template method pattern
            bodyContent = createBody();
        }

        page.add(new HtmlNonEscapedText("<!DOCTYPE html>"));

        final HtmlBranch html = new HtmlGenericElement("html");
        page.add(html);
        html.addAttribute("lang", Context.getLocalizator().getLanguageCode());
        pageHeader = new Header(getTitle(), getPageDescription(), getPageKeyWords(),  getRobots());
        html.add(pageHeader);

        html.add(bodyContent);

        // Set the last stable page into the session
        // Abstract method cf: template method pattern
        if (isStable()) {
            Context.getSession().setTargetPage(null);
            Context.getSession().setLastStablePage(thisUrl);
        }
        Context.getSession().setLastVisitedPage(thisUrl);

        // Display waiting notifications
        // Abstract method cf: template method pattern
        addWaitingNotifications();

        for(final HtmlElement meta : getMetas()){
            pageHeader.addMeta(meta);
        }

        // Do not forget to add the css/js files.
        for (final String css : page.getAllCss()) {
            pageHeader.addCss(css);
        }

        for (final String js : page.getAllJs()) {
            pageHeader.addJs(js);
        }

        for (final HtmlHeaderLink link : getLinks()) {
            pageHeader.addHeaderLink(link);
        }

        for (final HtmlNode node : page.getAllPostNode()) {
            bodyContent.add(node);
        }

        return page;
    }

    private void addWaitingNotifications() {
        for (final ErrorMessage notification : Context.getSession().getNotifications()) {
            final HtmlNotification note = new HtmlNotification(notification.getLevel());

            if (notification.getCreationDate().before(DateUtils.nowMinusSomeSeconds(SECONDS_BEFORE_SHOWING_MESSAGE_PREFIX))) {
                final TimeRenderer timeRenderer = new TimeRenderer(DateUtils.elapsed(DateUtils.now(), notification.getCreationDate()));
                timeRenderer.setPrefixSuffix(true);
                note.addText(timeRenderer.render(FormatStyle.SHORT) + " - ");
            }
            note.add(notification.getMessage());
            addNotification(note);
        }
        Context.getSession().flushNotifications();
    }
}
