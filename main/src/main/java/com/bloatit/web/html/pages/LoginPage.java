/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.html.pages;

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlPasswordField;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.LoginPageUrl;

@ParamContainer("login")
public class LoginPage extends Page {

    public LoginPage(LoginPageUrl loginPageUrl) throws RedirectException {
        super();
    }

    @Override
    public void create() throws RedirectException {
        super.create();

        final HtmlForm loginForm = new HtmlForm(new LoginActionUrl().toString());
        final HtmlTextField loginField = new HtmlTextField(LoginAction.LOGIN_CODE , session.tr("login"));
        final HtmlPasswordField passwordField = new HtmlPasswordField(LoginAction.PASSWORD_CODE , session.tr("password"));
        final HtmlSubmit submitButton = new HtmlSubmit(session.tr("Login"));

        loginForm.add(loginField);
        loginForm.add(passwordField);
        loginForm.add(submitButton);

        final HtmlTitleBlock loginTitle = new HtmlTitleBlock(session.tr("Login"), 2);
        loginTitle.add(loginForm);

        final HtmlTitleBlock signupTitle = new HtmlTitleBlock(session.tr("Signup"), 2);
        signupTitle.add(new HtmlText("Not yet implemented."));

        final HtmlGenericElement group = new HtmlGenericElement();

        group.add(loginTitle);
        group.add(signupTitle);

        add(group);
    }

    @Override
    protected String getTitle() {
        return "Login or signup";
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
