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

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;

@ParamContainer("ideas/create")
public class CreateIdeaPage extends LoggedPage {

    public CreateIdeaPage(final CreateIdeaPageUrl url) throws RedirectException {
        super(url);
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
    public HtmlElement generateRestrictedContent() {
        if (DemandManager.canCreate(session.getAuthToken())) {
            return generateDemandCreationForm();
        } else {
            return generateBadRightError();
        }
    }

    private HtmlElement generateDemandCreationForm() {

        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    private HtmlElement generateBadRightError() {
        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    @Override
    public String getRefusalReason() {
        return session.tr("You must be logged to create a new idea.");
    }
}
