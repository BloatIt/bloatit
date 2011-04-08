package com.bloatit.web.pages.master;

import static com.bloatit.framework.webserver.Context.tr;

import java.util.ArrayList;
import java.util.List;

import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlGenericElement;
import com.bloatit.framework.webserver.components.HtmlImage;
import com.bloatit.framework.webserver.components.PlaceHolderElement;
import com.bloatit.framework.webserver.components.advanced.HtmlClearer;
import com.bloatit.framework.webserver.components.meta.HtmlBranch;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.framework.webserver.components.meta.HtmlText;
import com.bloatit.framework.webserver.components.meta.XmlNode;
import com.bloatit.framework.webserver.masters.GenericPage;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.web.WebConfiguration;
import com.bloatit.web.url.IndexPageUrl;

public abstract class MasterPage extends GenericPage {

    private final HtmlBranch content;
    private HtmlBranch notifications;
    private final HtmlDiv notificationBlock;

    public MasterPage(final Url url) {
        super(url);
        content = new HtmlDiv().setId("content");
        notifications = null;
        notificationBlock = new HtmlDiv("notifications");
    }

    /**
     * TODO Documenter
     * @throws RedirectException
     */
    protected abstract void doCreate() throws RedirectException;

    @Override
    protected final void generateBody(final HtmlGenericElement body) throws RedirectException {
        final HtmlBranch header = new HtmlDiv("header").setId("header");
        body.add(header);
        final HtmlBranch headerContent = new HtmlDiv("header_content").setId("header_content");
        header.add(headerContent);
        header.add(new HtmlClearer());

        headerContent.add(generateLogo());
        headerContent.add(new SessionBar());

        body.add(new Menu());

        final HtmlBranch page = new HtmlDiv("page").setId("page");
        body.add(page);
        page.add(content);
        
        final PlaceHolderElement breacrumbPlaceHolder = new PlaceHolderElement();
        content.add(breacrumbPlaceHolder);
        content.add(notificationBlock);

        body.add(new Footer());
        doCreate();
        
        breacrumbPlaceHolder.add(generateBreadcrumb());
    }

    private XmlNode generateBreadcrumb() {

        return getBreadcrumb().toXmlNode();
    }

    @Override
    protected final String getTitle() {
        return "Elveos – " + getPageTitle();
    }

    protected abstract String getPageTitle();

    protected abstract Breadcrumb getBreadcrumb();

    @Override
    public final HtmlElement addAttribute(final String name, final String value) {
        content.addAttribute(name, value);
        return this;
    }

    @Override
    public final HtmlElement add(final XmlNode html) {
        content.add(html);
        return this;
    }

    @Override
    public final HtmlElement addText(final String text) {
        content.add(new HtmlText(text));
        return this;
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
