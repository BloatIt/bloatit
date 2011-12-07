/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * service: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.oauth2;

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
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateExternalServiceActionUrl;
import com.bloatit.web.url.CreateExternalServicePageUrl;

/**
 * Page that hosts the form to create a new feature
 */
@ParamContainer("service/create")
public final class CreateExternalServicePage extends LoggedElveosPage {
    private static final int DESCRIPTION_INPUT_NB_LINES = 10;
    private static final int DESCRIPTION_INPUT_NB_COLUMNS = 80;

    private final CreateExternalServicePageUrl url;

    public CreateExternalServicePage(final CreateExternalServicePageUrl url) {
        super(url);
        this.url = url;
    }

    @Override
    protected String createPageTitle() {
        return "Add a service";
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
        layout.addRight(new SideBarDocumentationBlock("service"));
        return layout;
    }

    private HtmlElement generateFeatureCreationForm() {
        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(Context.tr("Add a new service"), 1);
        final CreateExternalServiceActionUrl targetUrl = new CreateExternalServiceActionUrl(getSession().getShortKey());

        // Create the form stub
        final HtmlForm form = new HtmlForm(targetUrl.urlString());
        form.enableFileUpload();

        createFeatureTitle.add(form);

        // Create the field for the name of the service
        final FieldData serviceNameData = targetUrl.getServiceNameParameter().pickFieldData();
        final HtmlTextField serviceNameInput = new HtmlTextField(serviceNameData.getName(), Context.tr("Service name"));
        serviceNameInput.setDefaultValue(serviceNameData.getSuggestedValue());
        serviceNameInput.addErrorMessages(serviceNameData.getErrorMessages());
        serviceNameInput.setComment(Context.tr("The name of the service or application that will be able to connect to elveos."));
        form.add(serviceNameInput);

        // Description
        final FieldData descriptionData = targetUrl.getDescriptionParameter().pickFieldData();
        final MarkdownEditor descriptionInput = new MarkdownEditor(descriptionData.getName(),
                                                                   Context.tr("Describe the service"),
                                                                   DESCRIPTION_INPUT_NB_LINES,
                                                                   DESCRIPTION_INPUT_NB_COLUMNS);
        descriptionInput.setDefaultValue(descriptionData.getSuggestedValue());
        descriptionInput.addErrorMessages(descriptionData.getErrorMessages());
        descriptionInput.setComment(Context.tr("Mininum 10 character. Enter a short description of your service or application."));
        form.add(descriptionInput);

        // Language
        final FieldData languageData = targetUrl.getLangParameter().pickFieldData();
        final LanguageSelector languageInput = new LanguageSelector(languageData.getName(), Context.tr("Language"));
        languageInput.setDefaultValue(languageData.getSuggestedValue(), Context.getLocalizator().getLanguageCode());
        languageInput.addErrorMessages(languageData.getErrorMessages());
        languageInput.setComment(Context.tr("The language of your description."));
        form.add(languageInput);

        final HtmlFileInput serviceImageInput = new HtmlFileInput(CreateExternalServiceAction.IMAGE_CODE, Context.tr("Service logo"));
        serviceImageInput.setComment("Optional. The logo must be an image on a usable license, in png with transparency for the background. The size must be inferior to 64px x 64px.");
        form.add(serviceImageInput);

        form.add(new HtmlSubmit(Context.tr("submit")));

        final HtmlDiv group = new HtmlDiv();
        group.add(createFeatureTitle);
        return group;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to add a new service.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CreateExternalServicePage.generateBreadcrumb();
    }

    private static Breadcrumb generateBreadcrumb() {
        final Breadcrumb breadcrumb = OAuthPage.generateBreadcrumb();
        breadcrumb.pushLink(new CreateExternalServicePageUrl().getHtmlLink(tr("Add a service")));
        return breadcrumb;
    }
}
