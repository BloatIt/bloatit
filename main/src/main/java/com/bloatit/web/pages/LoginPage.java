/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.pages;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlParagraph;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlPasswordField;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.url.PageNotFoundUrl;
import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.pages.master.MasterPage;
import com.bloatit.web.url.LoginActionUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.RegisterPageUrl;

@ParamContainer("login")
public final class LoginPage extends MasterPage {

    public LoginPage(final LoginPageUrl loginPageUrl) {
        super(loginPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv box = new HtmlDiv("padding_box");
        {
            LoginActionUrl loginActionUrl = new LoginActionUrl();
            final HtmlForm loginForm = new HtmlForm(loginActionUrl.urlString());

            // Login field
            FormFieldData<String> loginData = loginActionUrl.getLoginParameter().createFormFieldData();
            final HtmlTextField loginField = new HtmlTextField(loginData, Context.trc("Login (noun)", "Login"));
            // passwordField
            final HtmlPasswordField passwordField = new HtmlPasswordField(LoginAction.PASSWORD_CODE, Context.tr("Password"));
            // sublit.
            final HtmlSubmit submitButton = new HtmlSubmit(Context.trc("Login (verb)", "Login"));

            loginForm.add(loginField);
            loginForm.add(passwordField);
            loginForm.add(submitButton);

            final HtmlTitleBlock loginTitle = new HtmlTitleBlock(Context.trc("Login (verb)", "Login"), 1);
            loginTitle.add(loginForm);

            box.add(loginTitle);

            box.add(new HtmlParagraph().add(new RegisterPageUrl().getHtmlLink(Context.tr("No account ? Sign-in now."))));

            box.add(new HtmlParagraph().add(new PageNotFoundUrl().getHtmlLink(Context.tr("Password lost ?"))));

        }
        add(box);

    }

    @Override
    protected String getPageTitle() {
        return Context.trc("Login (verb)", "Login");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
