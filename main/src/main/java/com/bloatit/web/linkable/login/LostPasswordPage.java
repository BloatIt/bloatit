package com.bloatit.web.linkable.login;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LostPasswordActionUrl;
import com.bloatit.web.url.LostPasswordPageUrl;

@ParamContainer("password/recover")
public class LostPasswordPage extends MasterPage {
    private final LostPasswordPageUrl url;

    public LostPasswordPage(LostPasswordPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        TwoColumnLayout layout = new TwoColumnLayout(true, url);
        LostPasswordActionUrl targetUrl = new LostPasswordActionUrl();
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        layout.addLeft(form);

        // EMAIL
        FieldData emailFieldData = targetUrl.getEmailParameter().pickFieldData();
        HtmlTextField emailInput = new HtmlTextField(emailFieldData.getName(), Context.tr("Enter your email"));
        emailInput.setDefaultValue(emailFieldData.getSuggestedValue());
        form.add(emailInput);

        form.add(new HtmlSubmit(Context.tr("Submit")));
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
