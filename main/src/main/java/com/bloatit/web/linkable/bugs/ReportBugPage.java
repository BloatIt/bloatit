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

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.right.AuthenticatedUserToken;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("feature/bug/report")
public final class ReportBugPage extends CreateUserContentPage {
    public static final int FILE_MAX_SIZE_MIO = 2;
    private static final int BUG_DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int BUG_DESCRIPTION_INPUT_NB_COLUMNS = 80;

    @SuppressWarnings("unused")
    @RequestParam
    private final Offer offer;

    private final Milestone milestone;

    private final ReportBugPageUrl url;

    public ReportBugPage(final ReportBugPageUrl url) {
        super(url, (computeActionUrl(computeMilestone(url))));
        this.url = url;
        milestone = computeMilestone(url);
        offer = url.getOffer();
    }

    private static Milestone computeMilestone(final ReportBugPageUrl url) {
        if (url.getOffer() == null) {
            return null;
        }
        if (url.getOffer().isFinished()) {
            return url.getOffer().getLastMilestone();
        }
        return url.getOffer().getCurrentMilestone();
    }

    private static ReportBugActionUrl computeActionUrl(final Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return new ReportBugActionUrl(milestone);
    }

    @Override
    protected String createPageTitle() {
        return "Report a bug";
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateReportBugForm(loggedUser));
        layout.addRight(new SideBarFeatureBlock(milestone.getOffer().getFeature(), new AuthenticatedUserToken(loggedUser)));
        layout.addRight(new SideBarDocumentationBlock("markdown"));
        return layout;
    }

    private HtmlElement generateReportBugForm(final Member loggedUser) {
        final HtmlTitleBlock formTitle = new HtmlTitleBlock(Context.tr("Report a bug"), 1);
        final ReportBugActionUrl doReportUrl = new ReportBugActionUrl(milestone);

        // Create the form stub
        final HtmlForm reportBugForm = new HtmlForm(doReportUrl.urlString());
        formTitle.add(reportBugForm);

        // title of the bug
        final FieldData bugTitleFieldData = doReportUrl.getTitleParameter().pickFieldData();
        final HtmlTextField bugTitleInput = new HtmlTextField(bugTitleFieldData.getName(), Context.tr("Bug title"));
        bugTitleInput.setDefaultValue(bugTitleFieldData.getSuggestedValue());
        bugTitleInput.setComment(Context.tr("A short title of the bug."));
        bugTitleInput.addErrorMessages(bugTitleFieldData.getErrorMessages());
        reportBugForm.add(bugTitleInput);

        // As team
        addAsTeamField(reportBugForm,
                       loggedUser,
                       UserTeamRight.TALK,
                       Context.tr("In the name of "),
                       Context.tr("Write this bug report in the name of this group."));

        // descriptions of the bug
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
        addLanguageField(reportBugForm, Context.tr("Language"), Context.tr("Language of the description."));

        // Level
        final FieldData levelFieldData = doReportUrl.getLevelParameter().pickFieldData();
        final HtmlDropDown levelInput = new HtmlDropDown(levelFieldData.getName(), Context.tr("Level"));
        levelInput.setDefaultValue(levelFieldData.getSuggestedValue());
        levelInput.addErrorMessages(levelFieldData.getErrorMessages());
        levelInput.addDropDownElements(EnumSet.allOf(BindedLevel.class));
        levelInput.setComment(Context.tr("Level of the bug."));
        reportBugForm.add(levelInput);

        // File
        reportBugForm.add(new AttachmentField(doReportUrl, FILE_MAX_SIZE_MIO + " Mio"));
        reportBugForm.enableFileUpload();

        reportBugForm.add(new HtmlSubmit(Context.tr("Report the bug")));

        final HtmlDiv group = new HtmlDiv();
        group.add(formTitle);
        return group;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to report a new bug.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return ReportBugPage.generateBreadcrumb(milestone.getOffer());
    }

    private static Breadcrumb generateBreadcrumb(final Offer offer) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbBugs(offer.getFeature());

        breadcrumb.pushLink(new ReportBugPageUrl(offer).getHtmlLink(tr("Report a bug")));

        return breadcrumb;
    }
}
