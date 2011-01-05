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

import com.bloatit.framework.managers.DemandManager;
import com.bloatit.web.actions.CreateIdeaAction;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.HtmlElement;
import com.bloatit.web.html.components.standard.HtmlDiv;
import com.bloatit.web.html.components.standard.HtmlTitleBlock;
import com.bloatit.web.html.components.standard.form.HtmlDropDown;
import com.bloatit.web.html.components.standard.form.HtmlForm;
import com.bloatit.web.html.components.standard.form.HtmlFormBlock;
import com.bloatit.web.html.components.standard.form.HtmlSubmit;
import com.bloatit.web.html.components.standard.form.HtmlTextArea;
import com.bloatit.web.html.components.standard.form.HtmlTextField;
import com.bloatit.web.utils.url.CreateIdeaActionUrl;
import com.bloatit.web.utils.url.CreateIdeaPageUrl;

@ParamContainer("idea/create")
public class CreateIdeaPage extends LoggedPage {

    @RequestParam(name = CreateIdeaAction.DESCRIPTION_CODE, defaultValue = "", role = Role.SESSION)
    private String description;
    
    @RequestParam(name = CreateIdeaAction.SPECIFICATION_CODE, defaultValue = "", role = Role.SESSION)
    private String specification;

    @RequestParam(name = CreateIdeaAction.PROJECT_CODE, defaultValue = "", role = Role.SESSION)
    private String project;

    @RequestParam(name = CreateIdeaAction.CATEGORY_CODE, defaultValue = "", role = Role.SESSION)
    private String category;

    @RequestParam(name = CreateIdeaAction.LANGUAGE_CODE, defaultValue = "", role = Role.SESSION)
    private String lang;
    
    public CreateIdeaPage(final CreateIdeaPageUrl createIdeaPageUrl) throws RedirectException {
        super(createIdeaPageUrl);
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
    public HtmlElement createRestrictedContent() {
        if (DemandManager.canCreate(session.getAuthToken())) {
            return generateIdeaCreationForm();
        } else {
            return generateBadRightError();
        }
    }

    private HtmlElement generateIdeaCreationForm() {
        HtmlTitleBlock createIdeaTitle = new HtmlTitleBlock(session.tr("Create a new idea"), 1);
        final CreateIdeaActionUrl doCreateUrl = new CreateIdeaActionUrl();

        // Create the form stub
        HtmlForm createIdeaForm = new HtmlForm(doCreateUrl.urlString());
        HtmlFormBlock specifBlock = new HtmlFormBlock(session.tr("Specify the new idea"));
        HtmlFormBlock paramBlock = new HtmlFormBlock(session.tr("Parameters of the new idea"));

        createIdeaTitle.add(createIdeaForm);
        createIdeaForm.add(specifBlock);
        createIdeaForm.add(paramBlock);
        createIdeaForm.add(new HtmlSubmit(session.tr("submit")));

        // Create the fields that will describe the specification of the idea (description & specification)
        HtmlTextField descriptionInput = new HtmlTextField(CreateIdeaAction.DESCRIPTION_CODE, session.tr("Title"));
        HtmlTextArea specificationInput = new HtmlTextArea(CreateIdeaAction.SPECIFICATION_CODE, session.tr("Describe the idea"), 10, 80);
        specifBlock.add(descriptionInput);
        specifBlock.add(specificationInput);

        // Create the fields that will be used to describe the parameters of the idea (project ...)
        HtmlDropDown languageInput = new HtmlDropDown(CreateIdeaAction.LANGUAGE_CODE, session.tr("Language"));
        languageInput.add(session.getLocale().getDisplayLanguage(), session.getLocale().getLanguage());
        
        HtmlTextField categoryInput = new HtmlTextField(CreateIdeaAction.CATEGORY_CODE, session.tr("Category"));
        HtmlTextField projectInput = new HtmlTextField(CreateIdeaAction.PROJECT_CODE, session.tr("Project"));
        paramBlock.add(languageInput);
        paramBlock.add(categoryInput);
        paramBlock.add(projectInput);
        
        final HtmlDiv group = new HtmlDiv();
        group.add(createIdeaTitle);
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
