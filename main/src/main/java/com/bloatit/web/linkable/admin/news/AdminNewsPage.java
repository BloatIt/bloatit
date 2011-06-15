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
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.SideBarElementLayout;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminHomePageUrl;
import com.bloatit.web.url.AdminNewsActionUrl;
import com.bloatit.web.url.AdminNewsDeleteActionUrl;
import com.bloatit.web.url.AdminNewsPageUrl;
import com.bloatit.web.url.AdminNewsRestoreActionUrl;

@ParamContainer("admin/news")
public class AdminNewsPage extends AdminPage {
    private AdminNewsPageUrl url;

    public AdminNewsPage(AdminNewsPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        TwoColumnLayout master = new TwoColumnLayout(true, url);
        AdminNewsActionUrl targetUrl = new AdminNewsActionUrl();
        master.addLeft(new HtmlTitle("Post a message to elveos news feed", 1));

        // Form to post a new message
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.addLeft(form);

        UrlParameter<String, String> messageParameter = targetUrl.getMessageParameter();
        HtmlTextField messageInput = new HtmlTextField(messageParameter.getName(), Context.tr("Message"));
        messageInput.setDefaultValue(messageParameter.getDefaultValue());
        messageInput.addErrorMessages(messageParameter.getMessages());
        messageInput.setComment("10-140 characters");
        form.add(messageInput);
        form.add(new HtmlSubmit(Context.tr("Submit")));

        // Small documentation paragraph at the bottom of the page
        HtmlDiv doc = new HtmlDiv();
        master.addLeft(doc);
        doc.addText("You can create a new entry in the elveos news feed using the form above. "
                + "This entry will be displayed on elveos home page, and also be pushed to the following micro-blogging social services :");
        HtmlList microBlogList = new HtmlList();
        doc.add(microBlogList);
        doc.addText("Once the message is posted, it can be deleted. However deletion will only erase the message on the website, and not remove"
                + "it from the various micro blogs. It is then advised to think carefully before creating a message.");
        for (String mb : MicroBlogManager.getMicroBlogNames()) {
            microBlogList.add(mb);
        }

        // Side bar where we display the last posts
        SideBarElementLayout sbel = new SideBarElementLayout();
        sbel.add(new HtmlTitle(Context.tr("Previous messages"), 1));
        master.addRight(sbel);
        for (NewsFeed news : NewsFeedManager.getAll()) {
            HtmlDiv div = new HtmlDiv();
            sbel.add(div);
            if (!news.isDeleted()) {
                AdminNewsDeleteActionUrl deleteUrl = new AdminNewsDeleteActionUrl(news);
                HtmlMixedText mixed = new HtmlMixedText(Context.tr("{0} (<0::delete>)", news.getMessage()), deleteUrl.getHtmlLink());
                div.add(mixed);
            } else {
                AdminNewsRestoreActionUrl deleteUrl = new AdminNewsRestoreActionUrl(news);
                HtmlSpan deletedMessageSpan = new HtmlSpan("deleted");
                HtmlMixedText mixed = new HtmlMixedText(Context.tr("<0:message:{0}> (<1::restore>)", news.getMessage()),
                                                        deletedMessageSpan,
                                                        deleteUrl.getHtmlLink());
                div.add(mixed);
            }
        }

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb(Member member) {
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
