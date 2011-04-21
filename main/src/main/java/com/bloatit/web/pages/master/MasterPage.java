package com.bloatit.web.pages.master;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.HashSet;
import java.util.Set;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Header;
import com.bloatit.framework.webprocessor.masters.Header.Robot;
import com.bloatit.framework.webprocessor.masters.HttpResponse.StatusCode;
import com.bloatit.framework.webprocessor.masters.Page;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.IndexPageUrl;

public abstract class MasterPage extends Page {

    private HtmlBranch notifications;
    private final HtmlDiv notificationBlock;

    public MasterPage(final Url url) {
        super(url);
        notifications = null;
        notificationBlock = new HtmlDiv("notifications");
    }

    // -----------------------------------------------------------------------
    // Template method pattern: Abstract methods
    // -----------------------------------------------------------------------
    protected abstract HtmlElement createBodyContent() throws RedirectException;

    protected abstract String createPageTitle();

    protected abstract Breadcrumb createBreadcrumb();

    // There is a default behavior here. You can overload it.
    protected HtmlElement createBodyContentOnParameterError() throws RedirectException {
        throw new PageNotFoundException();
    }

    // -----------------------------------------------------------------------
    // Template method pattern: Implementation
    // -----------------------------------------------------------------------
    @Override
    protected final HtmlElement createBody() throws RedirectException {
        return doCreateBody(createBodyContent());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns a default OK (200) status. <br />
     * Override to return an error.
     * </p>
     */
    @Override
    protected StatusCode getResponseStatus() {
        return StatusCode.OK_200;
    }

    @Override
    protected final HtmlElement createBodyOnParameterError() throws RedirectException {
        return doCreateBody(createBodyContentOnParameterError());
    }

    private HtmlElement doCreateBody(final HtmlElement bodyContent) throws RedirectException {
        final HtmlGenericElement body = new HtmlGenericElement("body");
        body.addCustomJs(FrameworkConfiguration.getJsJquery());
        body.addCustomJs(FrameworkConfiguration.getJsJqueryUi());
        body.addCustomJs(FrameworkConfiguration.getJsSelectivizr());
        body.addCustomJs(FrameworkConfiguration.getJsFlexie());

        final HtmlBranch header = new HtmlDiv("header").setId("header");
        body.add(header);
        final HtmlBranch headerContent = new HtmlDiv("header_content").setId("header_content");
        header.add(headerContent);
        header.add(new HtmlClearer());

        headerContent.add(new SessionBar());
        headerContent.add(generateLogo());

        body.add(new Menu());

        final HtmlBranch page = new HtmlDiv("page").setId("page");
        body.add(page);

        final HtmlBranch content = new HtmlDiv().setId("content");
        page.add(content);

        final PlaceHolderElement breacrumbPlaceHolder = new PlaceHolderElement();
        content.add(breacrumbPlaceHolder);
        content.add(notificationBlock);

        // Template method pattern.
        content.add(bodyContent);

        body.add(new Footer());

        breacrumbPlaceHolder.add(createBreadcrumb().toHtmlElement());
        return body;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Defines a generic description that should work for almost all pages, but
     * should be overloaded anyway
     * </p>
     */
    @Override
    protected String getPageDescription() {
        return Context.tr("Elveos is a platform where people gather to finance the development of open source software. "
                + "Elveos offers a streamlined process for the user, from the description of his needs, to the validation of the project ; "
                + "Elveos also guarantees that when you decide to fund a project, you will either get what you asked, or be reimbursed.");
    }

    @Override
    protected final String getTitle() {
        return "Elveos – " + createPageTitle();
    }

    @Override
    protected final void addNotification(final HtmlNotification note) {
        if (notifications == null) {
            notifications = new HtmlDiv().setId("notifications");
            notificationBlock.add(notifications);
        }
        notifications.add(note);
    }

    @Override
    protected Set<Robot> getRobots() {
        return new HashSet<Header.Robot>();
    }

    private HtmlElement generateLogo() {
        Context.getSession();

        final HtmlDiv logoDiv = new HtmlDiv("logo", "logo");

        final HtmlImage logoImage = new HtmlImage(new Image(WebConfiguration.getImgLogo()), tr("elveos.org logo"));
        logoImage.setCssClass("logo_elveos");

        logoDiv.add(new IndexPageUrl().getHtmlLink(logoImage));

        final HtmlSpan logoTextDiv = new HtmlSpan("logo_text", "logo_text");
        logoTextDiv.addText(tr("the cooperative platform for free software funding"));

        logoDiv.add(logoTextDiv);

        return logoDiv;
    }
}
