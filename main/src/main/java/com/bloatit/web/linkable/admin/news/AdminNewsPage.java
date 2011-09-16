package com.bloatit.web.linkable.admin.news;

import com.bloatit.framework.social.MicroBlogManager;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.Member;
import com.bloatit.model.NewsFeed;
import com.bloatit.model.managers.NewsFeedManager;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.SideBarElementLayout;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.AdminNewsActionUrl;
import com.bloatit.web.url.AdminNewsDeleteActionUrl;
import com.bloatit.web.url.AdminNewsPageUrl;
import com.bloatit.web.url.AdminNewsRestoreActionUrl;

@ParamContainer("admin/news")
public class AdminNewsPage extends AdminPage {
    private final AdminNewsPageUrl url;

    public AdminNewsPage(final AdminNewsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final TwoColumnLayout master = new TwoColumnLayout(true, url);
        final AdminNewsActionUrl targetUrl = new AdminNewsActionUrl(getSession().getShortKey());
        master.addLeft(new HtmlTitle("Post a message to elveos news feed", 1));

        // Form to post a new message
        final HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.addLeft(form);

        final UrlParameter<String, String> messageParameter = targetUrl.getMessageParameter();
        final HtmlTextField messageInput = new HtmlTextField(messageParameter.getName(), Context.tr("Message"));
        messageInput.setDefaultValue(messageParameter.getDefaultValue());
        messageInput.addErrorMessages(messageParameter.getMessages());
        messageInput.setComment("10-140 characters");
        form.add(messageInput);
        form.add(new HtmlSubmit(Context.tr("Submit")));

        // Small documentation paragraph at the bottom of the page
        final HtmlDiv doc = new HtmlDiv();
        master.addLeft(doc);
        doc.addText("You can create a new entry in the elveos news feed using the form above. "
                + "This entry will be displayed on elveos home page, and also be pushed to the following micro-blogging social services :");
        final HtmlList microBlogList = new HtmlList();
        doc.add(microBlogList);
        doc.addText("Once the message is posted, it can be deleted. However deletion will only erase the message on the website, and not remove"
                + "it from the various micro blogs. It is then advised to think carefully before creating a message.");
        for (final String mb : MicroBlogManager.getMicroBlogNames()) {
            microBlogList.add(mb);
        }

        // Side bar where we display the last posts
        final SideBarElementLayout sbel = new SideBarElementLayout();
        sbel.add(new HtmlTitle(Context.tr("Previous messages"), 1));
        master.addRight(sbel);
        for (final NewsFeed news : NewsFeedManager.getAll(true)) {
            final HtmlDiv div = new HtmlDiv();
            sbel.add(div);
            if (!news.isDeleted()) {
                final AdminNewsDeleteActionUrl deleteUrl = new AdminNewsDeleteActionUrl(getSession().getShortKey(), news);
                final HtmlMixedText mixed = new HtmlMixedText(Context.tr("{0} (<0::delete>)", news.getMessage()), deleteUrl.getHtmlLink());
                div.add(mixed);
            } else {
                final AdminNewsRestoreActionUrl deleteUrl = new AdminNewsRestoreActionUrl(getSession().getShortKey(), news);
                final HtmlSpan deletedMessageSpan = new HtmlSpan("deleted");
                final HtmlMixedText mixed = new HtmlMixedText(Context.tr("<0:message:{0}> (<1::restore>)", news.getMessage()),
                                                              deletedMessageSpan,
                                                              deleteUrl.getHtmlLink());
                div.add(mixed);
            }
        }

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb crumb = new Breadcrumb();
        crumb.pushLink(new AdminHomePageUrl().getHtmlLink("admin"));
        crumb.pushLink(new AdminNewsPageUrl().getHtmlLink("news"));
        return crumb;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Administration news feed page");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
