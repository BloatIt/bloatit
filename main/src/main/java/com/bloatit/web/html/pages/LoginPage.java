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
package com.bloatit.web.html.pages;

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlPasswordField;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;

@ParamContainer("login")
public final class LoginPage extends Page {

    public LoginPage(final LoginPageUrl loginPageUrl) {
        super(loginPageUrl);
    }

    @Override
    protected void doCreate() throws RedirectException {

        final HtmlDiv box = new HtmlDiv("padding_box");
        {
            final HtmlForm loginForm = new HtmlForm(new LoginActionUrl().urlString());
            final HtmlTextField loginField = new HtmlTextField(LoginAction.LOGIN_CODE, Context.trc("Login (noun)", "Login"));
            final HtmlPasswordField passwordField = new HtmlPasswordField(LoginAction.PASSWORD_CODE, Context.tr("Password"));
            final HtmlSubmit submitButton = new HtmlSubmit(Context.trc("Login (verb)", "Login"));

            loginForm.add(loginField);
            loginForm.add(passwordField);
            loginForm.add(submitButton);

            final HtmlTitleBlock loginTitle = new HtmlTitleBlock(Context.trc("Login (verb)", "Login"), 1);
            loginTitle.add(loginForm);

            box.add(loginTitle);
        }
        add(box);

    }

    @Override
    protected String getTitle() {
        return Context.trc("Login (verb)", "Login");
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
