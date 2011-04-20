package com.bloatit.web.linkable.login;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.RecoverPasswordPageUrl;

@ParamContainer("password/recover")
public class RecoverPasswordPage extends MasterPage {
    private final RecoverPasswordPageUrl url;

    public RecoverPasswordPage(RecoverPasswordPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(url);
        
        
        
        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Recover password");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = LoginPage.generateBreadcrumb();
        breadcrumb.pushLink(new LoginPageUrl().getHtmlLink(tr("Recover password")));
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
