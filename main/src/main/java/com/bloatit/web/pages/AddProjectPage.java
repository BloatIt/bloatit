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

import java.util.Map.Entry;

import com.bloatit.framework.utils.i18n.Localizator;
import com.bloatit.framework.utils.i18n.Localizator.LanguageDescriptor;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSimpleDropDown;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.web.actions.AddProjectAction;
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

    @RequestParam(name = AddProjectAction.SHORT_DESCRIPTION_CODE, defaultValue = "", role = Role.SESSION)
    private final String shortDescription;

    @RequestParam(name = AddProjectAction.DESCRIPTION_CODE, defaultValue = "", role = Role.SESSION)
    private final String description;

    @RequestParam(name = AddProjectAction.PROJECT_NAME_CODE, role = Role.SESSION)
    private final String projectName;


    @SuppressWarnings("unused")
    // Will be used when language can be changed on Idea creation
    @RequestParam(name = AddProjectAction.LANGUAGE_CODE, defaultValue = "", role = Role.SESSION)
    private final String lang;

    public AddProjectPage(final AddProjectPageUrl addProjectPageUrl) {
        super(addProjectPageUrl);
        this.description = addProjectPageUrl.getDescription();
        this.projectName = addProjectPageUrl.getProjectName();
        this.shortDescription = addProjectPageUrl.getShortDescription();
        this.lang = addProjectPageUrl.getLang();
    }

    @Override
    protected String getPageTitle() {
        return "Create new idea";
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
        final HtmlTextField projectNameInput = new HtmlTextField(AddProjectAction.PROJECT_NAME_CODE, Context.tr("Project name"));
        projectNameInput.setDefaultValue(projectName);
        projectNameInput.setComment(Context.tr("The name of the existing project."));
        addProjectForm.add(projectNameInput);

        // Create the fields that will describe the descriptions of the project
        final HtmlTextArea shortDescriptionInput = new HtmlTextArea(AddProjectAction.SHORT_DESCRIPTION_CODE, Context.tr("Describe briefly the project"),
                SHORT_DESCRIPTION_INPUT_NB_LINES, SHORT_DESCRIPTION_INPUT_NB_COLUMNS);
        shortDescriptionInput.setDefaultValue(shortDescription);
        shortDescriptionInput.setComment(Context.tr("Enter a short description of the projet in 120 characters."));
        addProjectForm.add(shortDescriptionInput);

        final HtmlTextArea descriptionInput = new HtmlTextArea(AddProjectAction.DESCRIPTION_CODE, Context.tr("Describe the project"),
                DESCRIPTION_INPUT_NB_LINES, DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setDefaultValue(description);
        descriptionInput.setComment(Context.tr("Mininum 10 character. You can enter a long description of the projet : list all features, add siteweb links, etc."));
        addProjectForm.add(descriptionInput);

        //Language
        final HtmlSimpleDropDown languageInput = new HtmlSimpleDropDown(AddProjectAction.LANGUAGE_CODE, Context.tr("Language"));
        for (final Entry<String, LanguageDescriptor> langEntry : Localizator.getAvailableLanguages().entrySet()) {
            languageInput.add(langEntry.getValue().name, langEntry.getValue().code);
        }
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
