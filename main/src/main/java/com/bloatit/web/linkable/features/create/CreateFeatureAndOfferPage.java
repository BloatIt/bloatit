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
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FieldData;
import com.bloatit.framework.webprocessor.components.form.Form;
import com.bloatit.framework.webprocessor.components.form.HtmlDateField;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlRadioButtonGroup;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Member;
import com.bloatit.web.components.LanguageSelector;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.softwares.SoftwaresTools;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.CreateFeatureAndOfferActionUrl;
import com.bloatit.web.url.CreateFeatureAndOfferPageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("feature/%process%/createwithoffer")
public final class CreateFeatureAndOfferPage extends CreateUserContentPage {

    public static final int FILE_MAX_SIZE_MIO = 2;

    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    @RequestParam(role = Role.PAGENAME)
    CreateFeatureProcess process;

    private final CreateFeatureAndOfferPageUrl url;

    public CreateFeatureAndOfferPage(final CreateFeatureAndOfferPageUrl url) {
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

        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Create a feature"), 1);

        // Create offer form
        final CreateFeatureAndOfferActionUrl targetUrl = new CreateFeatureAndOfferActionUrl(getSession().getShortKey(), process);
        final HtmlForm offerForm = new HtmlForm(targetUrl.urlString());

        Form f = new Form(CreateFeatureAndOfferAction.class, targetUrl);

        // Locale
        f.add(offerForm, new LanguageSelector(targetUrl.getLocaleParameter().getName()));

        // Title of the feature
        f.add(offerForm, new HtmlTextField(targetUrl.getDescriptionParameter().getName())).setCssClass("input_long_400px");

        // Price field
        f.add(offerForm, new HtmlMoneyField(targetUrl.getPriceParameter().getName()));

        // Linked software
        final FieldData softwareFieldData = targetUrl.getSoftwareParameter().pickFieldData();
        final FieldData newSoftwareNameFieldData = targetUrl.getNewSoftwareNameParameter().pickFieldData();
        final FieldData newSoftwareFieldData = targetUrl.getNewSoftwareParameter().pickFieldData();

        final SoftwaresTools.SoftwareChooserElement softwareInput = new SoftwaresTools.SoftwareChooserElement(softwareFieldData.getName(),
                                                                                                              newSoftwareNameFieldData.getName(),
                                                                                                              newSoftwareFieldData.getName(),
                                                                                                              Context.trc("Software (singular)",
                                                                                                                          "Software"));
        if (softwareFieldData.getSuggestedValue() != null) {
            softwareInput.setDefaultValue(softwareFieldData.getSuggestedValue());
        }

        if (newSoftwareNameFieldData.getSuggestedValue() != null) {
            softwareInput.setNewSoftwareDefaultValue(newSoftwareNameFieldData.getSuggestedValue());
        }

        if (newSoftwareFieldData.getSuggestedValue() != null) {
            softwareInput.setNewSoftwareCheckboxDefaultValue(newSoftwareFieldData.getSuggestedValue());
        }

        offerForm.add(softwareInput);

        // asTeam
        offerForm.add(new AsTeamField(targetUrl,
                                      loggedUser,
                                      UserTeamRight.TALK,
                                      Context.tr("In the name of "),
                                      Context.tr("Write this offer in the name of a team, and offer the contributions to this team.")));

        // Date field
        f.add(offerForm, new HtmlDateField(targetUrl.getExpiryDateParameter().getName(), Context.getLocalizator().getLocale()));

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
        HtmlFormField specifInput = f.add(offerForm, new HtmlTextArea(targetUrl.getSpecificationParameter().getName(), 10, 80));
        if (svalue == null || svalue.isEmpty()) {
            specifInput.setDefaultValue(suggestedValue);
        }

        // license
        final FieldData licenseData = targetUrl.getLicenseParameter().pickFieldData();
        final HtmlDropDown licenseInput = new HtmlDropDown(licenseData.getName());
        licenseInput.addDropDownElement("", Context.tr("Select a license…")).setDisabled().setSelected();
        licenseInput.addDropDownElement("Apache License 2.0", "Apache License 2.0");
        licenseInput.addDropDownElement("Artistic License/GPL", "Artistic License/GPL");
        licenseInput.addDropDownElement("GNU GPL v3", "GNU GPL v3");
        licenseInput.addDropDownElement("GNU GPL v2", "GNU GPL v2");
        licenseInput.addDropDownElement("GNU AGPL v3", "GNU AGPL v3");
        licenseInput.addDropDownElement("GNU Lesser GPL", "GNU Lesser GPL");
        licenseInput.addDropDownElement("MIT License", "MIT License");
        licenseInput.addDropDownElement("New BSD License", "New BSD License");
        licenseInput.addDropDownElement("Mozilla Public License 1.1", "Mozilla Public License 1.1");
        licenseInput.addDropDownElement("Eclipse Public License", "Eclipse Public License");
        licenseInput.addDropDownElement("Other Open Source", Context.tr("Other Open Source"));

        f.add(offerForm, licenseInput);
        licenseInput.setComment(new HtmlMixedText(Context.tr("Licenses must be <0::OSI-approved>. You should use one of the listed licenses to avoid <1::license proliferation>."),
                                                  new UrlString("http://opensource.org/licenses").getHtmlLink(),
                                                  new UrlString(Context.tr("http://en.wikipedia.org/wiki/License_proliferation")).getHtmlLink()));

        final HtmlDiv validationDetails = new HtmlDiv();
        final HtmlParagraph showHideLink = new HtmlParagraph(Context.tr("Show validation details"));
        showHideLink.setCssClass("fake_link");

        final JsShowHide showHideValidationDetails = new JsShowHide(offerForm,
                                                                    f.suggestedValueChanged(targetUrl.getPercentMajorParameter().getName())
                                                                            || f.suggestedValueChanged(targetUrl.getPercentFatalParameter().getName())
                                                                            || f.suggestedValueChanged(targetUrl.getDaysBeforeValidationParameter()
                                                                                                                .getName()));
        showHideValidationDetails.setHasFallback(false);
        showHideValidationDetails.addActuator(showHideLink);
        showHideValidationDetails.addListener(validationDetails);
        offerForm.add(showHideLink);
        offerForm.add(validationDetails);
        showHideValidationDetails.apply();

        // days before validation
        f.add(validationDetails, new HtmlTextField(targetUrl.getDaysBeforeValidationParameter().getName()));

        // percent Fatal
        f.add(validationDetails, new HtmlTextField(targetUrl.getPercentFatalParameter().getName()));

        // percent Major
        f.add(validationDetails, new HtmlTextField(targetUrl.getPercentMajorParameter().getName()));

        // Is finished
        final FieldData isFinishedData = targetUrl.getIsFinishedParameter().pickFieldData();
        final HtmlRadioButtonGroup isFinishedInput = new HtmlRadioButtonGroup(isFinishedData.getName());
        // isFinishedInput.addErrorMessages(isFinishedData.getErrorMessages());
        isFinishedInput.addRadioButton("true", Context.tr("Finish your Offer"));
        isFinishedInput.addRadioButton("false", Context.tr("Add another milestone"));
        offerForm.add(isFinishedInput);
        isFinishedInput.setDefaultValue(isFinishedData.getSuggestedValue());

        final HtmlSubmit offerButton = new HtmlSubmit(Context.tr("Validate !"));
        offerForm.add(offerButton);

        offerPageContainer.add(offerForm);

        layout.addLeft(offerPageContainer);

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
        return CreateFeatureAndOfferPage.generateBreadcrumb(process);
    }

    private static Breadcrumb generateBreadcrumb(CreateFeatureProcess process) {
        final Breadcrumb breadcrumb = FeatureListPage.generateBreadcrumb();
        breadcrumb.pushLink(new CreateFeaturePageUrl(process).getHtmlLink(tr("Create a feature")));
        return breadcrumb;
    }
}
