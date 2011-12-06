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

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.CreateReleaseActionUrl;
import com.bloatit.web.url.CreateReleasePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("release/create")
public final class CreateReleasePage extends CreateUserContentPage {

    private static final int DESCRIPTION_INPUT_NB_LINES = 5;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    @RequestParam(message = @tr("I cannot find the milestone number: ''%value%''."))
    @NonOptional(@tr("You have to specify a milestone number."))
    private final Milestone milestone;
    private final CreateReleasePageUrl url;

    public CreateReleasePage(final CreateReleasePageUrl url) {
        super(url);
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
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addRight(new SideBarFeatureBlock(milestone.getOffer().getFeature()));
        layout.addLeft(generateReleaseCreationForm());
        return layout;
    }

    private HtmlElement generateReleaseCreationForm() {
        final HtmlTitleBlock createReleaseTitle = new HtmlTitleBlock(tr("Add a new Release"), 1);

        final CreateReleaseActionUrl targetUrl = new CreateReleaseActionUrl(getSession().getShortKey(), milestone);

        // Create the form stub
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        FormBuilder ftool = new FormBuilder(CreateReleaseAction.class, targetUrl);
        createReleaseTitle.add(form);
        form.enableFileUpload();
        form.addLanguageChooser(targetUrl.getLocaleParameter().getName(), Context.getLocalizator().getLanguageCode());

        ftool.add(form, new HtmlTextField(targetUrl.getVersionParameter().getName()));
        ftool.add(form, new HtmlTextArea(targetUrl.getDescriptionParameter().getName(), DESCRIPTION_INPUT_NB_LINES, DESCRIPTION_INPUT_NB_COLUMNS));

        // Attachment
        AttachmentField attachment = new AttachmentField(targetUrl, "1 Gio", true);
        ftool.add(form, attachment.getFileInput());
        ftool.add(form, attachment.getTextInput());

        form.addSubmit(new HtmlSubmit(tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createReleaseTitle);
        return group;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CreateReleasePage.generateBreadcrumb(milestone);
    }

    private static Breadcrumb generateBreadcrumb(final Milestone milestone) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbOffers(milestone.getOffer().getFeature());

        breadcrumb.pushLink(new CreateReleasePageUrl(milestone).getHtmlLink(tr("Add a release")));

        return breadcrumb;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add a new release.");
    }
}
