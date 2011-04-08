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

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.DropDownElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Software;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("feature/create")
public final class CreateFeaturePage extends LoggedPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;
    private final CreateFeaturePageUrl url;

    public CreateFeaturePage(final CreateFeaturePageUrl url) {
        super(url);
        this.url = url;
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
    public void processErrors() throws RedirectException {
        // TODO maybe we should process the errors.
    }

    @Override
    public HtmlElement createRestrictedContent() {
        if (FeatureManager.canCreate(session.getAuthToken())) {

            add(generateFeatureCreationForm());
        }
        return generateBadRightError();
    }

    private HtmlElement generateFeatureCreationForm() {

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);


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
                session.notifyError(Context.tr("An error prevented us from displaying software information. Please notify us."));
                throw new ShallNotPassException("User cannot access software information", e);
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
        final String suggestedValue = tr("What is the expected work ?\n" +
        		"\n" +
        		"What is the requested date ?\n" +
        		"\n" +
        		"What is the expected compatibility ?\n" +
        		"\n" +
        		"  * Linux 2.6.x\n" +
        		"  * Windows 7\n" +
        		"  * Mac Os X\n" +
        		"  * ...\n" +
        		"\n" +
        		"What is the expected output ?\n" +
        		"\n" +
        		" * Source tarball\n" +
        		" * Diff patch\n" +
        		" * Public repository\n" +
        		" * Push in the project's official  repository\n" +
        		" * Windows install\n" +
        		" * Install shell script\n" +
        		" * Deb package\n" +
        		" * Rpm package\n" +
        		" * ...");

        specificationInput.setDefaultValue(suggestedValue);
        specificationInput.addErrorMessages(specificationFieldData.getErrorMessages());
        specificationInput.setComment(tr("Enter a long description of the feature : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        createFeatureForm.add(specificationInput);

        final FieldData languageFieldData = doCreateUrl.getLangParameter().pickFieldData();
        final LanguageSelector languageInput = new LanguageSelector(CreateFeatureAction.LANGUAGE_CODE, tr("Language"));
        languageInput.setDefaultValue(languageFieldData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        createFeatureForm.add(languageInput);

        // Submit button
        createFeatureForm.add(new HtmlSubmit(tr("submit")));

        layout.addLeft(createFeatureTitle);

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

    @Override
    protected Breadcrumb getBreadcrumb() {
        return CreateFeaturePage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();

        breadcrumb.pushLink(new CreateFeaturePageUrl().getHtmlLink(tr("Create a feature")));

        return breadcrumb;
    }
}
