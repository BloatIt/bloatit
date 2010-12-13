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
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlText;
import com.bloatit.web.html.components.standard.HtmlGenericElement;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlButton;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlPasswordField;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.html.pages.master.Page;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;


public class LoginPage extends Page {

    public LoginPage(final Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {

        final HtmlForm loginForm = new HtmlForm(new UrlBuilder(LoginAction.class).buildUrl());
        final HtmlTextField loginField = new HtmlTextField(LoginAction.LOGIN_CODE);
        final HtmlPasswordField passwordField = new HtmlPasswordField(LoginAction.LOGIN_CODE);
        final HtmlSubmit submitButton = new HtmlSubmit(session.tr("Login"));

        loginForm.add(loginField);
        loginForm.add(passwordField);
        loginForm.add(submitButton);

        final HtmlTitleBlock loginTitle = new HtmlTitleBlock(session.tr("Login"));
        loginTitle.add(loginForm);

        final HtmlTitleBlock sigupTitle = new HtmlTitleBlock(session.tr("Sigup"));
        sigupTitle.add(new HtmlText("Not yet implemented."));

        final HtmlGenericElement group = new HtmlGenericElement();

        group.add(loginTitle);
        group.add(sigupTitle);

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
