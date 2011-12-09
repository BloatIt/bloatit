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
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.HtmlHiddenableDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.ReportBugActionUrl;
import com.bloatit.web.url.ReportBugPageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("offers/%offer%/reportbug")
public final class ReportBugPage extends CreateUserContentPage {
    public static final int FILE_MAX_SIZE_MIO = 2;
    private static final int BUG_DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int BUG_DESCRIPTION_INPUT_NB_COLUMNS = 80;

    @RequestParam(role = Role.PAGENAME)
    private final Offer offer;

    private final Milestone milestone;

    private final ReportBugPageUrl url;

    public ReportBugPage(final ReportBugPageUrl url) {
        super(url);
        this.url = url;
        offer = url.getOffer();
        milestone = computeMilestone(offer);
    }

    private Milestone computeMilestone(final Offer offer) {
        if (offer == null) {
            return null;
        }
        if (offer.isFinished()) {
            return offer.getLastMilestone();
        }
        return offer.getCurrentMilestone();
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
        layout.addRight(new SideBarFeatureBlock(milestone.getOffer().getFeature()));
        layout.addRight(new SidebarMarkdownHelp());
        return layout;
    }

    private HtmlElement generateReportBugForm(final Member loggedUser) {
        final HtmlTitleBlock formTitle = new HtmlTitleBlock(Context.tr("Report a bug"), 1);
        final ReportBugActionUrl targetUrl = new ReportBugActionUrl(getSession().getShortKey(), milestone);

        // Create the form stub
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        formTitle.add(form);

        final FormBuilder ftool = new FormBuilder(ReportBugAction.class, targetUrl);

        form.addLanguageChooser(targetUrl.getLocaleParameter().getName(), Context.getLocalizator().getLanguageCode());
        form.addAsTeamField(new AsTeamField(targetUrl,
                                            loggedUser,
                                            UserTeamRight.TALK,
                                            Context.tr("In the name of "),
                                            Context.tr("Write this bug report in the name of this group.")));

        // title of the bug
        ftool.add(form, new HtmlTextField(targetUrl.getTitleParameter().getName()));
        ftool.add(form, new HtmlTextArea(targetUrl.getDescriptionParameter().getName(),
                                         BUG_DESCRIPTION_INPUT_NB_LINES,
                                         BUG_DESCRIPTION_INPUT_NB_COLUMNS));

        // Level
        final HtmlDropDown levelInput = new HtmlDropDown(targetUrl.getLevelParameter().getName());
        ftool.add(form, levelInput);
        levelInput.addDropDownElements(EnumSet.allOf(BindedLevel.class));

        // Attachment
        final AttachmentField attachment = new AttachmentField(targetUrl, FILE_MAX_SIZE_MIO + " Mio");
        final HtmlParagraph actuator = new HtmlParagraph(Context.tr("+ add attachement"), "fake_link");
        final HtmlHiddenableDiv hiddenable = new HtmlHiddenableDiv(actuator, false);
        form.add(actuator);
        form.add(hiddenable);
        ftool.add(hiddenable, attachment.getFileInput());
        ftool.add(hiddenable, attachment.getTextInput());

        form.addSubmit(new HtmlSubmit(Context.tr("Report the bug")));

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
