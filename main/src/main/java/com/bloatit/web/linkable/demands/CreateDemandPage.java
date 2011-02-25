/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.demands;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.DropDownElement;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlDropDown;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Project;
import com.bloatit.model.demand.DemandManager;
import com.bloatit.model.managers.ProjectManager;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.BoxLayout;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.CreateDemandActionUrl;
import com.bloatit.web.url.CreateDemandPageUrl;

/**
 * Page that hosts the form to create a new Idea
 */
@ParamContainer("demand/create")
public final class CreateDemandPage extends LoggedPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;

    public CreateDemandPage(final CreateDemandPageUrl createIdeaPageUrl) {
        super(createIdeaPageUrl);
    }

    @Override
    protected String getPageTitle() {
        return "Create new demand";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        if (DemandManager.canCreate(session.getAuthToken())) {

            add(generateIdeaCreationForm());
        }
        return generateBadRightError();
    }

    private HtmlElement generateIdeaCreationForm() {

        TwoColumnLayout layout = new TwoColumnLayout();

        BoxLayout box = new BoxLayout();

        final HtmlTitleBlock createIdeaTitle = new HtmlTitleBlock(tr("Create a new demand"), 1);
        final CreateDemandActionUrl doCreateUrl = new CreateDemandActionUrl();

        // Create the form stub
        final HtmlForm createIdeaForm = new HtmlForm(doCreateUrl.urlString());

        createIdeaTitle.add(createIdeaForm);

        // Create the fields that will describe the description of the idea
        final FieldData descriptionFieldData = doCreateUrl.getDescriptionParameter().fieldData();
        final HtmlTextField descriptionInput = new HtmlTextField(descriptionFieldData, tr("Title"));
        descriptionInput.setCssClass("input_long_400px");
        descriptionInput.setComment(tr("The title of the new idea must be permit to identify clearly the idea's specificity."));
        createIdeaForm.add(descriptionInput);

        // Linked project
        final HtmlDropDown projectInput = new HtmlDropDown(CreateDemandAction.PROJECT_CODE, Context.tr("Project"));
        for (final Project project : ProjectManager.getAll()) {
            try {
                projectInput.addDropDownElement(String.valueOf(project.getId()), project.getName());
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn(e);
                // Not display private projects
            }
        }
        // TODO: set the default value to "select a project"
        // TODO: add form to create a new project

        createIdeaForm.add(projectInput);

        // Description of the feature
        final FieldData specificationFieldData = doCreateUrl.getSpecificationParameter().fieldData();
        final HtmlTextArea specificationInput = new HtmlTextArea(specificationFieldData,
                                                                 tr("Describe the idea"),
                                                                 SPECIF_INPUT_NB_LINES,
                                                                 SPECIF_INPUT_NB_COLUMNS);
        specificationInput.setComment(tr("Enter a long description of the idea : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        createIdeaForm.add(specificationInput);

        final LanguageSelector languageInput = new LanguageSelector(CreateDemandAction.LANGUAGE_CODE, tr("Language"));
        createIdeaForm.add(languageInput);

        // Submit button
        createIdeaForm.add(new HtmlSubmit(tr("submit")));

        box.add(createIdeaTitle);

        layout.addLeft(box);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_demand"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    private HtmlElement generateBadRightError() {
        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new idea.");
    }

    /**
     * Class use to display projects in a dropdown html element
     */
    class ProjectElement implements DropDownElement {

        private final String name;
        private final String code;

        public ProjectElement(final Project project) throws UnauthorizedOperationException {
            name = project.getName();
            code = String.valueOf(project.getId());
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getCode() {
            return code;
        }

    }
}
