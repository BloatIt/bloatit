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

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.components.HtmlDiv;
import com.bloatit.framework.webserver.components.HtmlTitleBlock;
import com.bloatit.framework.webserver.components.form.FieldData;
import com.bloatit.framework.webserver.components.form.HtmlFileInput;
import com.bloatit.framework.webserver.components.form.HtmlForm;
import com.bloatit.framework.webserver.components.form.HtmlSubmit;
import com.bloatit.framework.webserver.components.form.HtmlTextArea;
import com.bloatit.framework.webserver.components.form.HtmlTextField;
import com.bloatit.framework.webserver.components.meta.HtmlElement;
import com.bloatit.model.Milestone;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.TwoColumnLayout;
import com.bloatit.web.url.AddReleaseActionUrl;
import com.bloatit.web.url.AddReleasePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("release/add")
public final class AddReleasePage extends LoggedPage {

    private static final int DESCRIPTION_INPUT_NB_LINES = 5;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    @RequestParam
    Milestone milestone;

    public AddReleasePage(final AddReleasePageUrl url) {
        super(url);
        milestone = url.getMilestone();
    }

    @Override
    protected String getPageTitle() {
        return tr("Add a release");
    }

    @Override
    public boolean isStable() {
        return false;
    }
    @Override
    public void processErrors() throws RedirectException {
    }
    @Override
    public HtmlElement createRestrictedContent() {
        final TwoColumnLayout layout = new TwoColumnLayout(true);
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
        final FieldData languageData = doCreateUrl.getLangParameter().pickFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageData.getName(), tr("Language"));
        languageInput.setDefaultValue(languageData.getSuggestedValue());
        languageInput.addErrorMessages(languageData.getErrorMessages());
        languageInput.setComment(tr("Language of the descriptions."));
        form.add(languageInput);

        // attachement
        final FieldData attachedFileData = doCreateUrl.getAttachedfileParameter().pickFieldData();
        final HtmlFileInput attachedFileInput = new HtmlFileInput(attachedFileData.getName(), tr("Attached file"));
        attachedFileInput.setDefaultValue(attachedFileData.getSuggestedValue());
        attachedFileInput.addErrorMessages(attachedFileData.getErrorMessages());
        attachedFileInput.setComment("You must attache a file. This is your release, it can take be a patch, a tar.gz etc.");
        form.add(attachedFileInput);

        form.add(new HtmlSubmit(tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createReleaseTitle);
        return group;
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return AddReleasePage.generateBreadcrumb(milestone);
    }

    public static Breadcrumb generateBreadcrumb(Milestone milestone) {
        Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbOffers(milestone.getOffer().getFeature());

        breadcrumb.pushLink(new AddReleasePageUrl(milestone).getHtmlLink(tr("Add a release")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new release.");
    }
}
