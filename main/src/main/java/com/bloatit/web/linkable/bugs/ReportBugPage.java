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
package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.util.EnumSet;

import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormBlock;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.pages.LoggedPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("feature/bug/report")
public final class ReportBugPage extends LoggedPage {
    private static final int BUG_DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int BUG_DESCRIPTION_INPUT_NB_COLUMNS = 80;

    public static final String BUG_BATCH = "bug_offer";

    @RequestParam(name = BUG_BATCH, role = Role.GET)
    private final Offer offer;

    private final ReportBugPageUrl url;

    public ReportBugPage(final ReportBugPageUrl url) {
        super(url);
        this.url = url;
        offer = url.getOffer();
    }

    @Override
    protected String getPageTitle() {
        return "Report a bug";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public void processErrors() throws RedirectException {
        // TODO you should process the errors.
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        if (FeatureManager.canCreate(session.getAuthToken())) {
            layout.addLeft(generateReportBugForm());
        } else {
            layout.addLeft(generateBadRightError());
        }

        layout.addRight(new SideBarFeatureBlock(offer.getFeature()));
        layout.addRight(new SideBarDocumentationBlock("markdown"));

        return layout;
    }

    private HtmlElement generateReportBugForm() {
        final HtmlTitleBlock formTitle = new HtmlTitleBlock(Context.tr("Report a bug"), 1);
        final ReportBugActionUrl doReportUrl = new ReportBugActionUrl(offer.getCurrentMilestone());

        // Create the form stub
        final HtmlForm reportBugForm = new HtmlForm(doReportUrl.urlString());
        reportBugForm.enableFileUpload();

        formTitle.add(reportBugForm);

        // Create the field for the title of the bug
        final FieldData bugTitleFieldData = doReportUrl.getTitleParameter().pickFieldData();
        final HtmlTextField bugTitleInput = new HtmlTextField(bugTitleFieldData.getName(), Context.tr("Bug title"));
        bugTitleInput.setDefaultValue(bugTitleFieldData.getSuggestedValue());
        bugTitleInput.setComment(Context.tr("A short title of the bug."));
        bugTitleInput.addErrorMessages(bugTitleFieldData.getErrorMessages());
        reportBugForm.add(bugTitleInput);

        // Create the fields that will describe the descriptions of the bug

        final FieldData descriptionFieldData = doReportUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextArea descriptionInput = new HtmlTextArea(descriptionFieldData.getName(),
                                                               Context.tr("Describe the bug"),
                                                               BUG_DESCRIPTION_INPUT_NB_LINES,
                                                               BUG_DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionFieldData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Mininum 10 character. You can enter a long description of the bug."));
        reportBugForm.add(descriptionInput);

        // Language
        final FieldData languageFieldData = doReportUrl.getLangParameter().pickFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageFieldData.getName(), Context.tr("Language"));
        languageInput.setDefaultValue(languageFieldData.getSuggestedValue());
        languageInput.addErrorMessages(languageFieldData.getErrorMessages());
        languageInput.setComment(Context.tr("Language of the description."));
        languageInput.setDefaultValue(languageFieldData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        reportBugForm.add(languageInput);

        // Level
        final FieldData levelFieldData = doReportUrl.getLevelParameter().pickFieldData();
        final HtmlDropDown levelInput = new HtmlDropDown(levelFieldData.getName(), Context.tr("Level"));
        levelInput.setDefaultValue(levelFieldData.getSuggestedValue());
        levelInput.addErrorMessages(levelFieldData.getErrorMessages());
        levelInput.addDropDownElements(EnumSet.allOf(BindedLevel.class));
        levelInput.setComment(Context.tr("Level of the bug."));
        reportBugForm.add(levelInput);

        // File
        final HtmlFormBlock attachementBlock = new HtmlFormBlock(tr("Attachement"));
        reportBugForm.add(attachementBlock);

        final HtmlFileInput attachementInput = new HtmlFileInput(ReportBugAction.ATTACHEMENT_CODE, Context.tr("Attachement file"));
        attachementInput.setComment("Optional. If attach a file, you must add an attachement description. Max 3MB.");
        attachementBlock.add(attachementInput);

        final FieldData attachementDescriptionFieldData = doReportUrl.getAttachementDescriptionParameter().pickFieldData();
        final HtmlTextField attachementDescriptionInput = new HtmlTextField(attachementDescriptionFieldData.getName(),
                                                                            Context.tr("Attachment description"));
        attachementDescriptionInput.setDefaultValue(attachementDescriptionFieldData.getSuggestedValue());
        attachementDescriptionInput.addErrorMessages(attachementDescriptionFieldData.getErrorMessages());
        attachementDescriptionInput.setComment(Context.tr("Need only if you add an attachement."));
        attachementBlock.add(attachementDescriptionInput);

        reportBugForm.add(new HtmlSubmit(Context.tr("Report the bug")));

        final HtmlDiv group = new HtmlDiv();
        group.add(formTitle);
        return group;
    }

    private HtmlElement generateBadRightError() {
        final HtmlDiv group = new HtmlDiv();

        return group;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to report a new bug.");
    }

    @Override
    protected Breadcrumb getBreadcrumb() {
        return ReportBugPage.generateBreadcrumb(offer);
    }

    public static Breadcrumb generateBreadcrumb(final Offer offer) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbBugs(offer.getFeature());

        breadcrumb.pushLink(new ReportBugPageUrl(offer).getHtmlLink(tr("Report a bug")));

        return breadcrumb;
    }
}
