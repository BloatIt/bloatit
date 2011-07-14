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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.linkable.usercontent.LanguageField;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("features/create")
public final class CreateFeaturePage extends CreateUserContentPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;
    public static final int FILE_MAX_SIZE_MIO = 2;
    private final CreateFeaturePageUrl url;

    public CreateFeaturePage(final CreateFeaturePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return "Create new feature";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        return generateFeatureCreationForm(loggedUser);
    }

    private HtmlElement generateFeatureCreationForm(final Member loggedUser) {

        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(tr("Create a new feature"), 1);
        final CreateFeatureActionUrl doCreateUrl = new CreateFeatureActionUrl(getSession().getShortKey());

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
        final FieldData softwareFieldData = doCreateUrl.getSoftwareParameter().pickFieldData();
        final HtmlDropDown softwareInput = new HtmlDropDown(softwareFieldData.getName(), Context.tr("Software"));

        softwareInput.addDropDownElement("", Context.tr("Select a software")).setDisabled().setSelected();
        softwareInput.addDropDownElement("", Context.tr("New software"));
        for (final Software software : SoftwareManager.getAll()) {
            softwareInput.addDropDownElement(String.valueOf(software.getId()), software.getName());
        }
        createFeatureForm.add(softwareInput);

        // As team input
        createFeatureForm.add(new AsTeamField(doCreateUrl,
                                              loggedUser,
                                              UserTeamRight.TALK,
                                              tr("In the name of"),
                                              tr("You can create this feature in the name of a team.")));

        // Description of the feature
        final FieldData specificationFieldData = doCreateUrl.getSpecificationParameter().pickFieldData();
        // final HtmlTextArea specificationInput = new
        // HtmlTextArea(specificationFieldData.getName(),
        final MarkdownEditor specificationInput = new MarkdownEditor(specificationFieldData.getName(),
                                                                     tr("Describe the feature"),
                                                                     SPECIF_INPUT_NB_LINES,
                                                                     SPECIF_INPUT_NB_COLUMNS);
        //@formatter:off
        final String suggestedValue = tr(
                "Be precise, don't forget to specify :\n" + 
        		" - The expected result\n" + 
        		" - On which system it has to work (Windows/Mac/Linux ...)\n" +
        		" - When do you want to have the result\n" + 
        		" - In which free license the result must be.\n" +
        		"\n" + 
        		"You can also join a diagram, or a design/mockup of the expected user interface.\n" +
        		"\n" + 
        		"Do not forget to specify if you want the result to be integrated upstream (in the official version of the software)"
        		);
        //@formatter:on

        if (specificationFieldData.getSuggestedValue() == null || specificationFieldData.getSuggestedValue().isEmpty()) {
            specificationInput.setDefaultValue(suggestedValue);
        } else {
            specificationInput.setDefaultValue(specificationFieldData.getSuggestedValue());
        }
        specificationInput.addErrorMessages(specificationFieldData.getErrorMessages());
        specificationInput.setComment(tr("Enter a long description of the feature : list all features, describe them all "
                + "... Try to leave as little room for ambiguity as possible."));
        createFeatureForm.add(specificationInput);

        // Language
        createFeatureForm.add(new LanguageField(doCreateUrl, tr("Description language"), tr("The language of the description you just wrote.")));

        // Attachment
        createFeatureForm.add(new AttachmentField(doCreateUrl, FILE_MAX_SIZE_MIO + " Mio"));

        // Submit button
        createFeatureForm.add(new HtmlSubmit(tr("submit")));

        // Markdown previewer
        final MarkdownPreviewer mdPreview = new MarkdownPreviewer(specificationInput);
        createFeatureTitle.add(mdPreview);

        layout.addLeft(createFeatureTitle);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_feature"));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new feature.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CreateFeaturePage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();

        breadcrumb.pushLink(new CreateFeaturePageUrl().getHtmlLink(tr("Create a feature")));

        return breadcrumb;
    }
}
