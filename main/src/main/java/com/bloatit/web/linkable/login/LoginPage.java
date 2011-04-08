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
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlPasswordField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.PageNotFoundUrl;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.web.linkable.meta.bugreport.SideBarBugReportBlock;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.SignUpPageUrl;

@ParamContainer("login")
public final class LoginPage extends MasterPage {
    private final LoginPageUrl url;

    public LoginPage(final LoginPageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected void doCreate() throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateSignUpPageMain());
        

        add(layout);
    }

    /**
     * @return
     */
    private HtmlElement generateSignUpPageMain() {
        final HtmlDiv master = new HtmlDiv();
        {
            final LoginActionUrl loginActionUrl = new LoginActionUrl();
            final HtmlForm loginForm = new HtmlForm(loginActionUrl.urlString());

            // Login field
            final FieldData loginData = loginActionUrl.getLoginParameter().pickFieldData();
            final HtmlTextField loginInput = new HtmlTextField(loginData.getName(), Context.trc("Login (noun)", "Login"));
            loginInput.setDefaultValue(loginData.getSuggestedValue());
            loginInput.addErrorMessages(loginData.getErrorMessages());
            loginInput.setComment(Context.tr("Note: This field is not case sensitive"));
            // passwordField
            final HtmlPasswordField passwordInput = new HtmlPasswordField(LoginAction.PASSWORD_CODE, Context.tr("Password"));
            // Submit
            final HtmlSubmit submitButton = new HtmlSubmit(Context.trc("Login (verb)", "Login"));

            loginForm.add(loginInput);
            loginForm.add(passwordInput);
            loginForm.add(submitButton);

            final HtmlTitleBlock loginTitle = new HtmlTitleBlock(Context.trc("Login (verb)", "Login"), 1);
            loginTitle.add(loginForm);

            master.add(loginTitle);
            master.add(new HtmlParagraph().add(new SignUpPageUrl().getHtmlLink(Context.tr("No account ? Sign-up now."))));
            master.add(new HtmlParagraph().add(new PageNotFoundUrl().getHtmlLink(Context.tr("Password lost ?"))));
        }
        return master;
    }

    @Override
    protected String getPageTitle() {
        return Context.trc("Login (verb)", "Login");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return LoginPage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        Breadcrumb breadcrumb = IndexPage.generateBreadcrumb();
        breadcrumb.pushLink(new LoginPageUrl().getHtmlLink(trc("Login (verb)", "Login")));
        return breadcrumb;
    }
}
