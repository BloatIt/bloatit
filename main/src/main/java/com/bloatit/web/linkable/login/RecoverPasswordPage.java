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
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.ElveosPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.RecoverPasswordActionUrl;
import com.bloatit.web.url.RecoverPasswordPageUrl;

/**
 * Page part of the password recovery process.
 * <p>
 * This page is displayed after the user clicked on the link in his email.
 * </p>
 */
@ParamContainer(value="member/password/recover", protocol=Protocol.HTTPS)
public class RecoverPasswordPage extends ElveosPage {
    private final RecoverPasswordPageUrl url;

    @RequestParam(role = Role.GET)
    private final String resetKey;

    @RequestParam(role = Role.GET)
    private final String login;

    public RecoverPasswordPage(final RecoverPasswordPageUrl url) {
        super(url);
        this.url = url;
        this.resetKey = url.getResetKey();
        this.login = url.getLogin();
    }

    @Override
    protected HtmlElement createBodyContent(final ElveosUserToken userToken) throws RedirectException {
        final Member member = MemberManager.getMemberByLogin(login);

        if (member == null || !member.getResetKey().equals(resetKey)) {
            getSession().notifyBad(Context.tr("The login and/or key are invalid, please verify you didn't do a mistake while cutting and pasting."));
            throw new PageNotFoundException();
        }

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock master = new HtmlTitleBlock(Context.tr("Password recovery"), 1);
        layout.addLeft(master);

        RecoverPasswordActionUrl targetUrl;
        targetUrl = new RecoverPasswordActionUrl(resetKey, member.getLogin());
        final HtmlForm form = new HtmlForm(targetUrl.urlString());
        master.add(form);

        final FieldData passwFieldData = targetUrl.getNewPasswordParameter().pickFieldData();
        final HtmlPasswordField passInput = new HtmlPasswordField(passwFieldData.getName(), Context.tr("New password"));
        passInput.setComment(Context.tr("Minimum 7 characters."));
        passInput.addErrorMessages(passwFieldData.getErrorMessages());
        form.add(passInput);

        final FieldData checkFieldData = targetUrl.getCheckNewPasswordParameter().pickFieldData();
        final HtmlPasswordField checkInput = new HtmlPasswordField(checkFieldData.getName(), Context.tr("Reenter password"));
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
    protected Breadcrumb createBreadcrumb(final ElveosUserToken userToken) {
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
