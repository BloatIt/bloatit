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
import com.bloatit.framework.webprocessor.components.form.HtmlDateField;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlForm;
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
import com.bloatit.model.Software;
import com.bloatit.model.managers.SoftwareManager;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeatureListPage;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.linkable.usercontent.LanguageField;
import com.bloatit.web.pages.master.Breadcrumb;
import com.bloatit.web.pages.master.sidebar.TwoColumnLayout;
import com.bloatit.web.url.CreateFeatureAndOfferActionUrl;
import com.bloatit.web.url.CreateFeatureAndOfferPageUrl;
import com.bloatit.web.url.CreateFeaturePageUrl;

/**
 * Page that hosts the form to create a new Feature
 */
@ParamContainer("feature/%process%/createwithoffer")
public final class CreateFeatureAndOfferPage extends CreateUserContentPage {

    private static final int SPECIF_INPUT_NB_LINES = 20;
    private static final int SPECIF_INPUT_NB_COLUMNS = 100;
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
        final CreateFeatureAndOfferActionUrl offerActionUrl = new CreateFeatureAndOfferActionUrl(getSession().getShortKey(), process);
        final HtmlForm offerForm = new HtmlForm(offerActionUrl.urlString());

        // Title of the feature
        final FieldData descriptionFieldData = offerActionUrl.getDescriptionParameter().pickFieldData();
        final HtmlTextField titleInput = new HtmlTextField(descriptionFieldData.getName(), tr("Title"));
        titleInput.setDefaultValue(descriptionFieldData.getSuggestedValue());
        titleInput.addErrorMessages(descriptionFieldData.getErrorMessages());
        titleInput.setCssClass("input_long_400px");
        titleInput.setComment(tr("The title of the new feature must be permit to identify clearly the feature's specificity."));
        offerForm.add(titleInput);
        
        // Price field
        final FieldData priceData = offerActionUrl.getPriceParameter().pickFieldData();
        final HtmlMoneyField priceInput = new HtmlMoneyField(priceData.getName(), Context.tr("Offer price"));
        priceInput.setDefaultValue(priceData.getSuggestedValue());
        priceInput.addErrorMessages(priceData.getErrorMessages());
        priceInput.setComment(Context.tr("The price must be in euros (€) and can't contains cents."));
        offerForm.add(priceInput);

        // Linked software
        final FieldData softwareFieldData = offerActionUrl.getSoftwareParameter().pickFieldData();
        final HtmlDropDown softwareInput = new HtmlDropDown(softwareFieldData.getName(), Context.tr("Software"));

        softwareInput.addDropDownElement("", Context.tr("Select a software")).setDisabled().setSelected();
        softwareInput.addDropDownElement("", Context.tr("New software"));
        for (final Software software : SoftwareManager.getAll()) {
            softwareInput.addDropDownElement(String.valueOf(software.getId()), software.getName());
        }
        softwareInput.setComment(Context.tr("On what software do you want to have this feature. Select 'new software' if your feature is the creation of a new software."));
        offerForm.add(softwareInput);
        
        // asTeam
        offerForm.add(new AsTeamField(offerActionUrl,
                                          loggedUser,
                                          UserTeamRight.TALK,
                                          Context.tr("In the name of "),
                                          Context.tr("Write this offer in the name of a team, and offer the contributions to this team.")));
        
        // Date field
        final FieldData dateData = offerActionUrl.getExpiryDateParameter().pickFieldData();
        final HtmlDateField dateInput = new HtmlDateField(dateData.getName(), Context.tr("Release date"), Context.getLocalizator().getLocale());
        dateInput.setDefaultValue(dateData.getSuggestedValue());
        dateInput.addErrorMessages(dateData.getErrorMessages());
        dateInput.setComment(Context.tr("You will have to release this feature before the release date."));
        offerForm.add(dateInput);

        // Specification
        final FieldData specificationData = offerActionUrl.getSpecificationParameter().pickFieldData();
        final HtmlTextArea specificationInput = new HtmlTextArea(specificationData.getName(), Context.tr("Description"), 10, 80);
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

        if (specificationData.getSuggestedValue() == null || specificationData.getSuggestedValue().isEmpty()) {
            specificationInput.setDefaultValue(suggestedValue);
        } else {
            specificationInput.setDefaultValue(specificationData.getSuggestedValue());
        }
        specificationInput.addErrorMessages(specificationData.getErrorMessages());
        specificationInput.setComment(Context.tr("Describe the feature and your offer. This description must be accurate because it will be used to validate the conformity at the end of the development."));
        offerForm.add(specificationInput);

        // license
        final FieldData licenseData = offerActionUrl.getLicenseParameter().pickFieldData();
        final HtmlDropDown licenseInput = new HtmlDropDown(licenseData.getName(), Context.tr("License"));
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
        licenseInput.setDefaultValue(licenseData.getSuggestedValue());
        licenseInput.addErrorMessages(licenseData.getErrorMessages());
        licenseInput.setComment(new HtmlMixedText(Context.tr("Licenses must be <0::OSI-approved>. You should use one of the listed licenses to avoid <1::license proliferation>."),
                                                  new UrlString("http://opensource.org/licenses").getHtmlLink(),
                                                  new UrlString(Context.tr("http://en.wikipedia.org/wiki/License_proliferation")).getHtmlLink()));

        offerForm.add(licenseInput);

        // locale
        offerForm.add(new LanguageField(offerActionUrl, //
                                        Context.tr("description language"), //
                                        Context.tr("The language of the description.")));

        final HtmlDiv validationDetails = new HtmlDiv();
        final HtmlParagraph showHideLink = new HtmlParagraph(Context.tr("Show validation details"));
        showHideLink.setCssClass("fake_link");

        final FieldData nbDaysData = offerActionUrl.getDaysBeforeValidationParameter().pickFieldData();
        final FieldData percentFatalData = offerActionUrl.getPercentFatalParameter().pickFieldData();
        final FieldData percentMajorData = offerActionUrl.getPercentMajorParameter().pickFieldData();
        final boolean percentMajorChanged = !(offerActionUrl.getPercentMajorParameter().getDefaultSuggestedValue().equals(percentMajorData.getSuggestedValue()));
        final boolean percentFatalChanged = !(offerActionUrl.getPercentFatalParameter().getDefaultSuggestedValue().equals(percentFatalData.getSuggestedValue()));
        final boolean daysBeforeValidationChanged = !(offerActionUrl.getDaysBeforeValidationParameter().getDefaultSuggestedValue().equals(nbDaysData.getSuggestedValue()));

        final JsShowHide showHideValidationDetails = new JsShowHide(offerForm, percentMajorChanged || percentFatalChanged || daysBeforeValidationChanged);
        showHideValidationDetails.setHasFallback(false);
        showHideValidationDetails.addActuator(showHideLink);
        showHideValidationDetails.addListener(validationDetails);

        offerForm.add(showHideLink);
        offerForm.add(validationDetails);
        showHideValidationDetails.apply();

        // days before validation
        final HtmlTextField nbDaysInput = new HtmlTextField(nbDaysData.getName(), Context.tr("Days before validation"));
        nbDaysInput.setDefaultValue(nbDaysData.getSuggestedValue());
        nbDaysInput.addErrorMessages(nbDaysData.getErrorMessages());
        nbDaysInput.setComment(Context.tr("The number of days to wait before this offer is can be validated. "
                + "During this time users can add bugs un the bug tracker. Fatal bugs have to be closed before the validation."));
        validationDetails.add(nbDaysInput);

        // percent Fatal
        final HtmlTextField percentFatalInput = new HtmlTextField(percentFatalData.getName(), Context.tr("Percent gained when no FATAL bugs"));
        percentFatalInput.setDefaultValue(percentFatalData.getSuggestedValue());
        percentFatalInput.addErrorMessages(percentFatalData.getErrorMessages());
        percentFatalInput.setComment(Context.tr("If you want to add some warranty to the contributor you can say that you want to gain less than 100% "
                + "of the amount on this feature request when all the FATAL bugs are closed. "
                + "The money left will be transfered when all the MAJOR bugs are closed. If you specify this field, you have to specify the next one on MAJOR bug percent. "
                + "By default, all the money on this feature request is transfered when all the FATAL bugs are closed."));
        validationDetails.add(percentFatalInput);

        // percent Major
        final HtmlTextField percentMajorInput = new HtmlTextField(percentMajorData.getName(), Context.tr("Percent gained when no MAJOR bugs"));
        percentMajorInput.setDefaultValue(percentMajorData.getSuggestedValue());
        percentMajorInput.addErrorMessages(percentMajorData.getErrorMessages());
        percentMajorInput.setComment(Context.tr("If you specified a value for the 'FATAL bugs percent', you have to also specify one for the MAJOR bugs. "
                + "You can say that you want to gain less than 100% of the amount on this offer when all the MAJOR bugs are closed. "
                + "The money left will be transfered when all the MINOR bugs are closed. Make sure that (FATAL percent + MAJOR percent) <= 100."));
        validationDetails.add(percentMajorInput);

        // Is finished
        final FieldData isFinishedData = offerActionUrl.getIsFinishedParameter().pickFieldData();
        final HtmlRadioButtonGroup isFinishedInput = new HtmlRadioButtonGroup(isFinishedData.getName());
        // isFinishedInput.addErrorMessages(isFinishedData.getErrorMessages());
        isFinishedInput.addRadioButton("true", Context.tr("Finish your Offer"));
        isFinishedInput.addRadioButton("false", Context.tr("Add an other lot"));
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
