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
package com.bloatit.web.linkable.features;

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
import com.bloatit.model.Software;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.master.BoxLayout;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("feature/create")
public final class CreateFeaturePage extends LoggedPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;

    public CreateFeaturePage(final CreateFeaturePageUrl createFeaturePageUrl) {
        super(createFeaturePageUrl);
    }

    @Override
    protected String getPageTitle() {
        return "Create new feature";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent() {
        if (FeatureManager.canCreate(session.getAuthToken())) {

            add(generateFeatureCreationForm());
        }
        return generateBadRightError();
    }

    private HtmlElement generateFeatureCreationForm() {

        final TwoColumnLayout layout = new TwoColumnLayout();

        final BoxLayout box = new BoxLayout();

        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(tr("Create a new feature"), 1);
        final CreateFeatureActionUrl doCreateUrl = new CreateFeatureActionUrl();

        // Create the form stub
        final HtmlForm createFeatureForm = new HtmlForm(doCreateUrl.urlString());

        createFeatureTitle.add(createFeatureForm);

        // Create the fields that will describe the description of the feature
        final FieldData descriptionFieldData = doCreateUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextField descriptionInput = new HtmlTextField(descriptionFieldData.getName(), tr("Title"));
        descriptionInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionFieldData.getErrorMessages());
        descriptionInput.setCssClass("input_long_400px");
        descriptionInput.setComment(tr("The title of the new feature must be permit to identify clearly the feature's specificity."));
        createFeatureForm.add(descriptionInput);

        // Linked software
        final HtmlDropDown softwareInput = new HtmlDropDown(CreateFeatureAction.SOFTWARE_CODE, Context.tr("Software"));
        for (final Software software : SoftwareManager.getAll()) {
            try {
                softwareInput.addDropDownElement(String.valueOf(software.getId()), software.getName());
            } catch (final UnauthorizedOperationException e) {
                Log.web().warn(e);
                // Not display private softwarets
            }
        }
        // TODO: set the default value to "select a software"
        // TODO: add form to create a new software

        createFeatureForm.add(softwareInput);

        // Description of the feature
        final FieldData specificationFieldData = doCreateUrl.getSpecificationParameter().pickFieldData();
        final HtmlTextArea specificationInput = new HtmlTextArea(specificationFieldData.getName(),
                                                                 tr("Describe the feature"),
                                                                 SPECIF_INPUT_NB_LINES,
                                                                 SPECIF_INPUT_NB_COLUMNS);
        specificationInput.setDefaultValue(specificationFieldData.getSuggestedValue());
        specificationInput.addErrorMessages(specificationFieldData.getErrorMessages());
        specificationInput.setComment(tr("Enter a long description of the feature : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        createFeatureForm.add(specificationInput);

        final LanguageSelector languageInput = new LanguageSelector(CreateFeatureAction.LANGUAGE_CODE, tr("Language"));
        createFeatureForm.add(languageInput);

        // Submit button
        createFeatureForm.add(new HtmlSubmit(tr("submit")));

        box.add(createFeatureTitle);

        layout.addLeft(box);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_feature"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    private HtmlElement generateBadRightError() {
        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new feature.");
    }

    /**
     * Class use to display softwares in a dropdown html element
     */
    class SoftwareElement implements DropDownElement {

        private final String name;
        private final String code;

        public SoftwareElement(final Software software) throws UnauthorizedOperationException {
            name = software.getName();
            code = String.valueOf(software.getId());
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
