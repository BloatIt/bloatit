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

import com.bloatit.framework.managers.DemandManager;
import java.util.HashMap;
import java.util.Map;

import com.bloatit.web.actions.LoginAction;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlButton;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlComponent;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlContainer;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlForm;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlPasswordField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlText;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTextField;
import com.bloatit.web.htmlrenderer.htmlcomponent.HtmlTitle;
import com.bloatit.web.server.Page;
import com.bloatit.web.server.Session;

public class CreateIdeaPage extends LoggedPage {

    public CreateIdeaPage(Session session) {
        this(session, new HashMap<String, String>());
    }

    public CreateIdeaPage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    public String getCode() {
        return "idea/create";
    }

    @Override
    protected String getTitle() {
        return "Create new idea";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlComponent generateRestrictedContent() {
         if(DemandManager.canCreate(session.getAuthToken())) {
                return generateDemandCreationForm();
            } else {
                return generateBadRightError();
            }
    }

    private HtmlComponent generateDemandCreationForm() {


        final LoginAction logAction = new LoginAction(session);

        final HtmlForm loginForm = new HtmlForm(logAction);
        final HtmlTextField loginField = new HtmlTextField();
        final HtmlPasswordField passwordField = new HtmlPasswordField();
        final HtmlButton submitButton = new HtmlButton(session.tr("Create idea"));

        loginForm.add(loginField);
        loginForm.add(passwordField);
        loginForm.add(submitButton);

        loginField.setName(logAction.getLoginCode());
        passwordField.setName(logAction.getPasswordCode());

        final HtmlTitle loginTitle = new HtmlTitle(session.tr("Create a new idea"), "");
        loginTitle.add(loginForm);

        
        final HtmlContainer group = new HtmlContainer();

        group.add(loginTitle);

        return group;
    }

    private HtmlComponent generateBadRightError() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getRefusalReason() {
        return session.tr("You must be logged to create a new idea.");
    }
}
