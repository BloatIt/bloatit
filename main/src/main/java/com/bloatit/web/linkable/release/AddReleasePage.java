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
package com.bloatit.web.linkable.release;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.AddReleaseActionUrl;
import com.bloatit.web.url.AddReleasePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("release/add")
public final class AddReleasePage extends CreateUserContentPage {

    private static final int DESCRIPTION_INPUT_NB_LINES = 5;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    @RequestParam(conversionErrorMsg = @tr("I cannot find the milestone number: ''%value''."))
    @ParamConstraint(optionalErrorMsg = @tr("You have to specify a milestone number."))
    Milestone milestone;
    private final AddReleasePageUrl url;

    public AddReleasePage(final AddReleasePageUrl url) {
        super(url, new AddReleaseActionUrl(url.getMilestone()));
        this.url = url;
        milestone = url.getMilestone();
    }

    @Override
    protected String createPageTitle() {
        return tr("Add a release");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public void processErrors() throws RedirectException {
        if (!url.getMessages().isEmpty()) {
            throw new PageNotFoundException();
        }
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addRight(new SideBarFeatureBlock(milestone.getOffer().getFeature()));
        layout.addLeft(generateReleaseCreationForm());
        return layout;
    }

    private HtmlElement generateReleaseCreationForm() {
        final HtmlTitleBlock createReleaseTitle = new HtmlTitleBlock(tr("Add a new Release"), 1);

        final AddReleaseActionUrl doCreateUrl = new AddReleaseActionUrl(milestone);

        // Create the form stub
        final HtmlForm form = new HtmlForm(doCreateUrl.urlString());
        form.enableFileUpload();

        createReleaseTitle.add(form);

        // version
        final FieldData versionData = doCreateUrl.getVersionParameter().pickFieldData();
        final HtmlTextField versionInput = new HtmlTextField(versionData.getName(), tr("Version"));
        versionInput.setDefaultValue(versionData.getSuggestedValue());
        versionInput.addErrorMessages(versionData.getErrorMessages());
        versionInput.setComment(tr("Enter your release version. For example ''1.2.3''."));
        form.add(versionInput);

        // description
        final FieldData descriptionData = doCreateUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionData.getName(),
                                                               tr("Comment your release"),
                                                               DESCRIPTION_INPUT_NB_LINES,
                                                               DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(tr("Enter a short comment on your release."));
        form.add(descriptionInput);

        // Language
        addLanguageField(form, tr("Language"), tr("Language of the descriptions."));

        // attachment
        addAddAttachmentField(form,
                              tr("Attached file"),
                              tr("Add here a file for your release."),
                              tr("File description"),
                              tr("Add a little description of the file."));

        form.add(new HtmlSubmit(tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createReleaseTitle);
        return group;
    }

    @Override
    protected Breadcrumb createBreadcrumb() {
        return AddReleasePage.generateBreadcrumb(milestone);
    }

    public static Breadcrumb generateBreadcrumb(final Milestone milestone) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbOffers(milestone.getOffer().getFeature());

        breadcrumb.pushLink(new AddReleasePageUrl(milestone).getHtmlLink(tr("Add a release")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new release.");
    }
}
