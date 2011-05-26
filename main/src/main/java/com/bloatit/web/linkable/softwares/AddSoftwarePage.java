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
package com.bloatit.web.linkable.softwares;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AddSoftwareActionUrl;
import com.bloatit.web.url.AddSoftwarePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("software/add")
public final class AddSoftwarePage extends LoggedPage {
    private static final int DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    private final AddSoftwarePageUrl url;

    public AddSoftwarePage(final AddSoftwarePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return "Add a software";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateFeatureCreationForm());
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SideBarDocumentationBlock("software"));
        return layout;
    }

    private HtmlElement generateFeatureCreationForm() {
        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(Context.tr("Add a new software"), 1);
        final AddSoftwareActionUrl doCreateUrl = new AddSoftwareActionUrl();

        // Create the form stub
        final HtmlForm addSoftwareForm = new HtmlForm(doCreateUrl.urlString());
        addSoftwareForm.enableFileUpload();

        createFeatureTitle.add(addSoftwareForm);

        // Create the field for the name of the software
        final FieldData softwareNameData = doCreateUrl.getSoftwareNameParameter().pickFieldData();
        final HtmlTextField softwareNameInput = new HtmlTextField(softwareNameData.getName(), Context.tr("Software name"));
        softwareNameInput.setDefaultValue(softwareNameData.getSuggestedValue());
        softwareNameInput.addErrorMessages(softwareNameData.getErrorMessages());
        softwareNameInput.setComment(Context.tr("The name of the existing software."));
        addSoftwareForm.add(softwareNameInput);

        // Description
        final FieldData descriptionData = doCreateUrl.getDescriptionParameter().pickFieldData();
        final MarkdownEditor descriptionInput = new MarkdownEditor(descriptionData.getName(),
                                                                   Context.tr("Describe the software"),
                                                                   DESCRIPTION_INPUT_NB_LINES,
                                                                   DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Mininum 10 character. You can enter a long description of the project : list all features, add siteweb links, etc."));
        addSoftwareForm.add(descriptionInput);

        // Language
        final FieldData languageData = doCreateUrl.getLangParameter().pickFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageData.getName(), Context.tr("Language"));
        languageInput.setDefaultValue(languageData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        languageInput.addErrorMessages(languageData.getErrorMessages());
        languageInput.setComment(Context.tr("Language of the descriptions."));
        addSoftwareForm.add(languageInput);

        final HtmlFileInput softwareImageInput = new HtmlFileInput(AddSoftwareAction.IMAGE_CODE, Context.tr("Software logo"));
        softwareImageInput.setComment("Optional. The logo must be an image on a usable license, in png with transparency for the background. The size must be inferior to 64px x 64px.");
        addSoftwareForm.add(softwareImageInput);

        addSoftwareForm.add(new HtmlSubmit(Context.tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createFeatureTitle);
        return group;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to add a new software.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return AddSoftwarePage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = SoftwareListPage.generateBreadcrumb();
        breadcrumb.pushLink(new AddSoftwarePageUrl().getHtmlLink(tr("Add a software")));
        return breadcrumb;
    }
}
