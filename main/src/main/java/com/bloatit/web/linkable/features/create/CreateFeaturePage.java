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
package com.bloatit.web.linkable.features.create;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.advanced.showdown.MarkdownEditor;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.Form;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.HtmlHiddenableDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.Member;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.AttachmentField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.CreateFeatureActionUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

public final class CreateFeaturePage extends CreateUserContentPage {

    public static final int SPECIF_INPUT_NB_LINES = 20;
    public static final int SPECIF_INPUT_NB_COLUMNS = 100;
    public static final int FILE_MAX_SIZE_MIO = 2;

    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    @RequestParam(role = Role.PAGENAME)
    CreateFeatureProcess process;

    private final CreateFeaturePageUrl url;

    public CreateFeaturePage(final CreateFeaturePageUrl url) {
        super(url);
        this.url = url;
        this.process = url.getProcess();
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Create new feature");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        return generateFeatureCreationForm(loggedUser);
    }

    private HtmlElement generateFeatureCreationForm(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);

        final HtmlTitleBlock createFeatureTitle = new HtmlTitleBlock(tr("Create a new feature"), 1);
        final CreateFeatureActionUrl targetUrl = new CreateFeatureActionUrl(getSession().getShortKey(), process);

        // Create the form stub
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        form.enableFileUpload();
        createFeatureTitle.add(form);

        form.addLanguageChooser(targetUrl.getLocaleParameter().getName(), Context.getLocalizator().getLanguageCode());
        form.addAsTeamField(new AsTeamField(targetUrl,
                                            loggedUser,
                                            UserTeamRight.TALK,
                                            tr("In the name of"),
                                            tr("You can create this feature in the name of a team.")));

        Form formTool = new Form(CreateFeatureAction.class, targetUrl);

        formTool.add(form, new HtmlTextField(targetUrl.getDescriptionParameter().getName()));

        // Linked software
        final FieldData softwareFieldData = targetUrl.getSoftwareParameter().pickFieldData();
        final FieldData newSoftwareNameFieldData = targetUrl.getNewSoftwareNameParameter().pickFieldData();
        final FieldData newSoftwareFieldData = targetUrl.getNewSoftwareParameter().pickFieldData();
        final SoftwaresTools.SoftwareChooserElement softwareInput = new SoftwaresTools.SoftwareChooserElement(softwareFieldData.getName(),
                                                                                                              newSoftwareNameFieldData.getName(),
                                                                                                              newSoftwareFieldData.getName());
        formTool.add(form, softwareInput);
        if (softwareFieldData.getSuggestedValue() != null) {
            softwareInput.setDefaultValue(softwareFieldData.getSuggestedValue());
        }

        if (newSoftwareNameFieldData.getSuggestedValue() != null) {
            softwareInput.setNewSoftwareDefaultValue(newSoftwareNameFieldData.getSuggestedValue());
        }

        if (newSoftwareFieldData.getSuggestedValue() != null) {
            softwareInput.setNewSoftwareCheckboxDefaultValue(newSoftwareFieldData.getSuggestedValue());
        }

        //@formatter:off
        final String suggestedValue = tr(
                "Be precise, don't forget to specify :\n" +
                " - The expected result\n" +
                " - On which system it has to work (Windows/Mac/Linux ...)\n" +
                " - When do you want to have the result\n" +
                " - In which free license the result must be.\n" +
                "\n" +
                "You can also join a diagram, or a design/mockup of the expected user interface.\n" +
                "\n" +
                "Do not forget to specify if you want the result to be integrated upstream (in the official version of the software)"
                );
        //@formatter:on
        final String svalue = targetUrl.getSpecificationParameter().getSuggestedValue();
        HtmlFormField specifInput = formTool.add(form, new MarkdownEditor(targetUrl.getSpecificationParameter().getName(), 10, 80));
        if (svalue == null || svalue.isEmpty()) {
            specifInput.setDefaultValue(suggestedValue);
        }

        // Attachment
        AttachmentField attachment = new AttachmentField(targetUrl, FILE_MAX_SIZE_MIO + " Mio");
        HtmlParagraph actuator = new HtmlParagraph(Context.tr("+ add attachement"), "fake_link");
        HtmlHiddenableDiv hiddenable = new HtmlHiddenableDiv(actuator, false);
        form.add(actuator);
        form.add(hiddenable);
        formTool.add(hiddenable, attachment.getFileInput());
        formTool.add(hiddenable, attachment.getTextInput());

        // Submit button
        form.addSubmit(new HtmlSubmit(tr("submit")));

        layout.addLeft(createFeatureTitle);

        // RightColunm
        layout.addRight(new SideBarDocumentationBlock("create_feature"));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SidebarMarkdownHelp());

        return layout;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to create a new feature.");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return CreateFeaturePage.generateBreadcrumb(process);
    }

    private static Breadcrumb generateBreadcrumb(CreateFeatureProcess process) {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();
        breadcrumb.pushLink(new CreateFeaturePageUrl(process).getHtmlLink(tr("Create a feature")));
        return breadcrumb;
    }
}
