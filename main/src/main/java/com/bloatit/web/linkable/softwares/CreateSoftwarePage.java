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
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlFileInput;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateSoftwareActionUrl;
import com.bloatit.web.url.CreateSoftwarePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("software/create")
public final class CreateSoftwarePage extends LoggedElveosPage {
    private static final int DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    private final CreateSoftwarePageUrl url;

    public CreateSoftwarePage(final CreateSoftwarePageUrl url) {
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
        final CreateSoftwareActionUrl targetUrl = new CreateSoftwareActionUrl(getSession().getShortKey());

        // Create the form stub
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        form.enableFileUpload();
        createFeatureTitle.add(form);
        final FormBuilder ftool = new FormBuilder(CreateSoftwareAction.class, targetUrl);

        form.addLanguageChooser(targetUrl.getLangParameter().getName(), Context.getLocalizator().getLanguageCode());
        ftool.add(form, new HtmlTextField(targetUrl.getSoftwareNameParameter().getName()));
        final MarkdownEditor markdownEdit = new MarkdownEditor(targetUrl.getDescriptionParameter().getName(),
                                                               DESCRIPTION_INPUT_NB_LINES,
                                                               DESCRIPTION_INPUT_NB_COLUMNS);
        ftool.add(form, markdownEdit);
        ftool.add(form, new HtmlFileInput(CreateSoftwareAction.IMAGE_CODE));

        form.addSubmit(new HtmlSubmit(Context.tr("submit")));

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
        return CreateSoftwarePage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = SoftwareListPage.generateBreadcrumb();
        breadcrumb.pushLink(new CreateSoftwarePageUrl().getHtmlLink(tr("Add a software")));
        return breadcrumb;
    }
}
