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
package test.pages;


import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.actions.LoginAction;
import test.html.HtmlText;
import test.html.components.standard.HtmlGenericElement;
import test.html.components.standard.HtmlTitleBlock;
import test.html.components.standard.form.HtmlButton;
import test.html.components.standard.form.HtmlForm;
import test.html.components.standard.form.HtmlPasswordField;
import test.html.components.standard.form.HtmlTextField;
import test.pages.master.Page;

public class LoginPage extends Page {

    public LoginPage(Request request) throws RedirectException {
        super(request);
        generateContent();
    }

    private void generateContent() {
        
        final HtmlForm loginForm = new HtmlForm(new UrlBuilder(LoginAction.class).buildUrl());
        final HtmlTextField loginField = new HtmlTextField(LoginAction.LOGIN_CODE);
        final HtmlPasswordField passwordField = new HtmlPasswordField(LoginAction.LOGIN_CODE);
        final HtmlButton submitButton = new HtmlButton(session.tr("Login"));

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
