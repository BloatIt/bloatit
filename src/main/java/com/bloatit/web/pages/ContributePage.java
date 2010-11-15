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

import com.bloatit.web.actions.ContributionAction;
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

public class ContributePage extends Page {

    public ContributePage(Session session) {
        this(session, new HashMap<String, String>());
    }

    public ContributePage(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    @Override
    protected HtmlComponent generateContent() {
        final ContributionAction contribAction = new ContributionAction(session);

        final HtmlForm contribForm = new HtmlForm(contribAction);
        final HtmlTextField contribField = new HtmlTextField(session.tr("Choose amount : "));
        final HtmlButton submitButton = new HtmlButton(session.tr("Contribute"));

        contribForm.add(contribField);
        contribForm.add(submitButton);

        contribField.setName(contribAction.getContributionCode());

        final HtmlTitle contribTitle = new HtmlTitle(session.tr("Contribute"), "");
        contribTitle.add(contribForm);

        final HtmlContainer group = new HtmlContainer();

        group.add(contribTitle);

        return group;
    }

    @Override
    public String getCode() {
        return "contribute";
    }

    @Override
    protected String getTitle() {
        return "Contribute to a project";
    }

    @Override
    public boolean isStable() {
        return false;
    }
}
