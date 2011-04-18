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
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownPreviewer;
import com.bloatit.framework.webprocessor.components.form.DropDownElement;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Software;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("feature/create")
public final class CreateFeaturePage extends CreateUserContentPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;
    private final CreateFeaturePageUrl url;

    public CreateFeaturePage(final CreateFeaturePageUrl url) {
        super(url, new CreateFeatureActionUrl());
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
        if (FeatureManager.canCreate(session.getAuthToken())) {
            return generateFeatureCreationForm(loggedUser);
        }
        // TODO
        session.notifyBad("//TODO");
        return new HtmlParagraph(Context.tr("You are not allowed to create a new feature"));
    }

    private HtmlElement generateFeatureCreationForm(final Member loggedUser) {

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
        final FieldData softwareFieldData = doCreateUrl.getSoftwareParameter().pickFieldData();
        final HtmlDropDown softwareInput = new HtmlDropDown(softwareFieldData.getName(), Context.tr("Software"));
        for (final Software software : SoftwareManager.getAll()) {
            try {
                softwareInput.addDropDownElement(String.valueOf(software.getId()), software.getName());
            } catch (final UnauthorizedOperationException e) {
                session.notifyError(Context.tr("An error prevented us from displaying software information. Please notify us."));
                throw new ShallNotPassException("User cannot access software information", e);
            }
        }
        // TODO: set the default value to "select a software"
        createFeatureForm.add(softwareInput);

        // As team input
        addAsTeamField(createFeatureForm,
                       loggedUser,
                       UserTeamRight.TALK,
                       tr("In the name of"),
                       tr("You can create this feature in the name of a team."));

        // Description of the feature
        final FieldData specificationFieldData = doCreateUrl.getSpecificationParameter().pickFieldData();
        // final HtmlTextArea specificationInput = new
        // HtmlTextArea(specificationFieldData.getName(),
        final MarkdownEditor specificationInput = new MarkdownEditor(specificationFieldData.getName(),
                                                                     tr("Describe the feature"),
                                                                     SPECIF_INPUT_NB_LINES,
                                                                     SPECIF_INPUT_NB_COLUMNS);
        //@formatter:off
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
        addLanguageField(createFeatureForm, tr("Description language"), tr("The language of the description you just wrote."));

        // Attachment
        addAddAttachmentField(createFeatureForm, "2 Mio");

        // Submit button
        createFeatureForm.add(new HtmlSubmit(tr("submit")));

        // Markdown previewer
        final MarkdownPreviewer mdPreview = new MarkdownPreviewer(specificationInput);
        createFeatureTitle.add(mdPreview);

        layout.addLeft(createFeatureTitle);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_feature"));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new feature.");
    }

    /**
     * Class use to display softwares in a dropdown html element
     */
    private static class SoftwareElement implements DropDownElement {

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
    protected Breadcrumb createBreadcrumb() {
        return CreateFeaturePage.generateBreadcrumb();
    }

    public static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();

        breadcrumb.pushLink(new CreateFeaturePageUrl().getHtmlLink(tr("Create a feature")));

        return breadcrumb;
    }
}
