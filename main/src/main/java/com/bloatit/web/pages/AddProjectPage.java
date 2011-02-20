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

import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FormFieldData;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.web.actions.AddProjectAction;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.url.AddProjectActionUrl;
import com.bloatit.web.url.AddProjectPageUrl;

/**
 * Page that hosts the form to create a new Idea
 */
@ParamContainer("project/add")
public final class AddProjectPage extends LoggedPage {

    private static final int SHORT_DESCRIPTION_INPUT_NB_LINES = 3;
    private static final int SHORT_DESCRIPTION_INPUT_NB_COLUMNS = 80;

    private static final int DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    public AddProjectPage(final AddProjectPageUrl addProjectPageUrl) {
        super(addProjectPageUrl);
    }

    @Override
    protected String getPageTitle() {
        return "Add a project";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        if (DemandManager.canCreate(session.getAuthToken())) {
            return new HtmlDiv("padding_box").add(generateIdeaCreationForm());
        }
        return generateBadRightError();
    }

    private HtmlElement generateIdeaCreationForm() {
        final HtmlTitleBlock createIdeaTitle = new HtmlTitleBlock(Context.tr("Add a new project"), 1);
        final AddProjectActionUrl doCreateUrl = new AddProjectActionUrl();

        // Create the form stub
        final HtmlForm addProjectForm = new HtmlForm(doCreateUrl.urlString());
        addProjectForm.enableFileUpload();

        createIdeaTitle.add(addProjectForm);

        // Create the field for the name of the project
        final FormFieldData<String> createFormFieldData = doCreateUrl.getProjectNameParameter().formFieldData();
        final HtmlTextField projectNameInput = new HtmlTextField(createFormFieldData, Context.tr("Project name"));
        projectNameInput.setComment(Context.tr("The name of the existing project."));
        addProjectForm.add(projectNameInput);

        // Create the fields that will describe the descriptions of the project
        final FormFieldData<String> shortDescriptionFormFieldData = doCreateUrl.getShortDescriptionParameter().formFieldData();
        final HtmlTextArea shortDescriptionInput = new HtmlTextArea(shortDescriptionFormFieldData,
                                                                    Context.tr("Describe briefly the project"),
                                                                    SHORT_DESCRIPTION_INPUT_NB_LINES,
                                                                    SHORT_DESCRIPTION_INPUT_NB_COLUMNS);
        shortDescriptionInput.setComment(Context.tr("Enter a short description of the projet in 120 characters."));
        addProjectForm.add(shortDescriptionInput);

        final FormFieldData<String> descriptionFormFieldData = doCreateUrl.getDescriptionParameter().formFieldData();
        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionFormFieldData,
                                                               Context.tr("Describe the project"),
                                                               DESCRIPTION_INPUT_NB_LINES,
                                                               DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setComment(Context.tr("Mininum 10 character. You can enter a long description of the projet : list all features, add siteweb links, etc."));
        addProjectForm.add(descriptionInput);

        // Language
        final FormFieldData<String> languageFormFieldData = doCreateUrl.getLangParameter().formFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageFormFieldData, Context.tr("Language"));

        languageInput.setComment(Context.tr("Language of the descriptions."));
        addProjectForm.add(languageInput);

        final HtmlFileInput projectImageInput = new HtmlFileInput(AddProjectAction.IMAGE_CODE, Context.tr("Project logo"));
        projectImageInput.setComment("Optional. The logo must be an image on a usable licence, in png with transparency for the background. The size must be 50px × 50px.");
        addProjectForm.add(projectImageInput);

        addProjectForm.add(new HtmlSubmit(Context.tr("submit")));

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
        return Context.tr("You must be logged to add a new project.");
    }
}
