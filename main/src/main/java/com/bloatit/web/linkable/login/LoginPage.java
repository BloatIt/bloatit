/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.login;

import static com.bloatit.framework.webprocessor.context.Context.trc;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.ElveosPage;
import com.bloatit.web.linkable.master.sidebar.TitleSideBarElementLayout;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.IndexPageUrl;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.LostPasswordPageUrl;
import com.bloatit.web.url.SignUpPageUrl;

@ParamContainer(value = "member/login", protocol = Protocol.HTTPS)
public final class LoginPage extends ElveosPage {

    private final LoginPageUrl url;

    @RequestParam
    private final String returnUrl;

    @RequestParam
    @Optional
    private final Boolean invoice;

    public LoginPage(final LoginPageUrl url) {
        super(url);
        this.url = url;
        this.invoice = url.getInvoice();
        this.returnUrl = url.getReturnUrl();
    }

    @Override
    protected HtmlElement createBodyContent() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateSignUpPageMain());

        layout.addRight(new RecoverPasswordSideBarElement());

        return layout;
    }

    private HtmlElement generateSignUpPageMain() {
        final HtmlDiv master = new HtmlDiv();
        {
            final HtmlTitleBlock loginTitle = new HtmlTitleBlock(Context.trc("Login (verb)", "Login"), 1);
            final LoginActionUrl targetUrl = new LoginActionUrl(returnUrl);
            final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
            final FormBuilder ftool = new FormBuilder(LoginAction.class, targetUrl);
            loginTitle.add(form);

            // Login field
            ftool.add(form, new HtmlTextField(targetUrl.getLoginParameter().getName()));

            // passwordField
            ftool.add(form, new HtmlPasswordField(LoginAction.PASSWORD_CODE));

            // Submit
            final SignUpPageUrl signUpPageUrl = new SignUpPageUrl();
            signUpPageUrl.setInvoice(invoice);
            form.addSubmit(signUpPageUrl.getHtmlLink(Context.tr("Sign up")));
            form.addSubmit(new HtmlSubmit(Context.trc("Login (verb)", "Login")));

            master.add(loginTitle);
        }
        return master;
    }

    private static class RecoverPasswordSideBarElement extends TitleSideBarElementLayout {
        public RecoverPasswordSideBarElement() {
            setTitle(Context.tr("Password lost"));
            add(new HtmlMixedText(Context.tr("You can recover you password use the <0::password recovery form>."),
                                  new LostPasswordPageUrl().getHtmlLink()));
        }
    }

    @Override
    protected String createPageTitle() {
        return Context.trc("Login (verb)", "Login");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return LoginPage.generateBreadcrumb();
    }

    protected static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new LoginPageUrl(new IndexPageUrl().urlString()).getHtmlLink(trc("Login (verb)", "Login")));
        return breadcrumb;
    }
}
