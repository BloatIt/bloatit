package com.bloatit.web.pages.master;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlGenericElement;
import com.bloatit.framework.webprocessor.components.HtmlImage;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.advanced.HtmlClearer;
import com.bloatit.framework.webprocessor.components.meta.HtmlBranch;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.GenericPage;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.IndexPageUrl;

public abstract class MasterPage extends GenericPage {

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

    // protected abstract HtmlElement addNotifications();
    // -----------------------------------------------------------------------
    // Template method pattern: Implementation
    // -----------------------------------------------------------------------
    @Override
    protected final void createPageContent(final HtmlGenericElement body) throws RedirectException {
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
        content.add(createBodyContent());

        body.add(new Footer());

        breacrumbPlaceHolder.add(createBreadcrumb().toHtmlElement());
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

    @Override
    protected List<String> getCustomJs() {
        final List<String> customJsList = new ArrayList<String>();
        customJsList.add(FrameworkConfiguration.getJsJquery());
        customJsList.add(FrameworkConfiguration.getJsJquery());
        customJsList.add(FrameworkConfiguration.getJsSelectivizr());
        customJsList.add(FrameworkConfiguration.getJsFlexie());

        return customJsList;
    }
}
