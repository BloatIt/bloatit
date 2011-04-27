package com.bloatit.web.linkable.login;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.RecoverPasswordActionUrl;
import com.bloatit.web.url.RecoverPasswordPageUrl;

/**
 * Page part of the password recovery process.
 * <p>
 * This page is displayed after the user clicked on the link in his email.
 * </p>
 */
@ParamContainer(value="password/recover", protocol=Protocol.HTTPS)
public class RecoverPasswordPage extends MasterPage {
    private final RecoverPasswordPageUrl url;

    @RequestParam(role = Role.GET)
    private final String resetKey;

    @RequestParam(role = Role.GET)
    private final String login;

    public RecoverPasswordPage(RecoverPasswordPageUrl url) {
        super(url);
        this.url = url;
        this.resetKey = url.getResetKey();
        this.login = url.getLogin();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        Member member = MemberManager.getMemberByLogin(login);

        if (member == null || !member.getResetKey().equals(resetKey)) {
            session.notifyBad(Context.tr("The login and/or key are invalid, please verify you didn't do a mistake while cutting and pasting."));
            throw new PageNotFoundException();
        }

        TwoColumnLayout layout = new TwoColumnLayout(true, url);

        HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Password recovery"), 1);
        layout.addLeft(master);

        RecoverPasswordActionUrl targetUrl;
        targetUrl = new RecoverPasswordActionUrl(resetKey, member.getLogin());
        HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.add(form);

        FieldData passwFieldData = targetUrl.getNewPasswordParameter().pickFieldData();
        HtmlPasswordField passInput = new HtmlPasswordField(passwFieldData.getName(), Context.tr("New password"));
        passInput.setComment(Context.tr("Minimum 7 characters."));
        passInput.addErrorMessages(passwFieldData.getErrorMessages());
        form.add(passInput);

        FieldData checkFieldData = targetUrl.getCheckNewPasswordParameter().pickFieldData();
        HtmlPasswordField checkInput = new HtmlPasswordField(checkFieldData.getName(), Context.tr("Reenter password"));
        checkInput.addErrorMessages(checkFieldData.getErrorMessages());
        form.add(checkInput);

        form.add(new HtmlSubmit(Context.tr("Reset password")));

        return layout;
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Reset password");
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = LoginPage.generateBreadcrumb();
        return breadcrumb;
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
