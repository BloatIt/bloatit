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
package com.bloatit.web.pages;

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPasswordField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;
import java.util.HashMap;
import java.util.Map;

public class LoginPage extends Page {

    public LoginPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    public LoginPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    protected void generateContent() {
        HtmlForm loginForm = new HtmlForm();
        HtmlTextField loginField = new HtmlTextField();
        HtmlPasswordField passwordField =  new HtmlPasswordField();
        HtmlButton submitButton = new HtmlButton();

        loginForm.addComponent(loginField);
        loginForm.addComponent(passwordField);
        loginForm.addComponent(submitButton);

        LoginAction logAction = new LoginAction(this.session);

        loginForm.setAction(logAction);
        submitButton.setLabel(this.session.tr("Login"));
        loginField.setName(logAction.getLoginCode());
        passwordField.setName(logAction.getPasswordCode());


        this.htmlResult.write("<h2>"+this.session.tr("Login")+"</h2>");
        loginForm.generate(this.htmlResult);
        this.htmlResult.write("<h2>"+this.session.tr("Sigup")+"</h2>");
        this.htmlResult.write("<p>Not yet implemented.</p>");
    }

    @Override
    public String getCode() {
        return "login";
    }

    @Override
    protected String getTitle() {
        return "Login or signup";
    }
}