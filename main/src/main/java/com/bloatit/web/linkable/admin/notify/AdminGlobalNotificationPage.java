package com.bloatit.web.linkable.admin.notify;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.linkable.admin.master.AdminPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AdminGlobalNotificationActionUrl;
import com.bloatit.web.url.AdminGlobalNotificationPageUrl;
import com.bloatit.web.url.AdminHomePageUrl;

@ParamContainer("admin/notify")
public class AdminGlobalNotificationPage extends AdminPage {
    private final AdminGlobalNotificationPageUrl url;

    public AdminGlobalNotificationPage(final AdminGlobalNotificationPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createAdminContent() throws UnauthorizedOperationException {
        final TwoColumnLayout master = new TwoColumnLayout(true, url);

        final HtmlForm form = new HtmlForm(new AdminGlobalNotificationActionUrl(getSession().getShortKey()).urlString());
        master.addLeft(form);

        final UrlParameter<String, String> messageParameter = new AdminGlobalNotificationActionUrl(getSession().getShortKey()).getMessageParameter();
        final HtmlTextField messageInput = new HtmlTextField(messageParameter.getName(), Context.tr("Message"));
        if (messageParameter.getSuggestedValue() != null) {
            messageInput.setDefaultValue(messageParameter.getSuggestedValue());
        } else if (Context.getGlobalNotification() != null) {
            messageInput.setDefaultValue(Context.getGlobalNotification());
        }
        form.add(messageInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));

        return master;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        final Breadcrumb crumb = new Breadcrumb();
        crumb.pushLink(new AdminHomePageUrl().getHtmlLink("admin"));
        crumb.pushLink(new AdminGlobalNotificationPageUrl().getHtmlLink("notification"));
        return crumb;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Global notification administration page");
    }

    @Override
    public boolean isStable() {
        return true;
    }
}
