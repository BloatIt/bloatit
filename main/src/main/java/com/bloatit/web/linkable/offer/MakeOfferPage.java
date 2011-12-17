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
package com.bloatit.web.linkable.offer;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlDateField;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlHidden;
import com.bloatit.framework.webprocessor.components.form.HtmlMoneyField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextArea;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.UrlString;
import com.bloatit.model.Feature;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.components.SideBarFeatureBlock;
import com.bloatit.web.components.SidebarMarkdownHelp;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.features.FeaturePage;
import com.bloatit.web.linkable.features.OfferBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.usercontent.AsTeamField;
import com.bloatit.web.linkable.usercontent.CreateUserContentPage;
import com.bloatit.web.url.MakeOfferPageUrl;
import com.bloatit.web.url.OfferActionUrl;

@ParamContainer("offer/create")
public final class MakeOfferPage extends CreateUserContentPage {

    @RequestParam(message = @tr("I cannot find the feature number: ''%value%''."))
    @NonOptional(@tr("The feature id is not optional !"))
    private final Feature feature;

    @RequestParam
    @Optional
    private final Offer offer;

    private final MakeOfferPageUrl url;

    public MakeOfferPage(final MakeOfferPageUrl url) {
        super(url);
        this.url = url;
        this.feature = url.getFeature();
        this.offer = url.getOffer();
    }

    @Override
    protected String createPageTitle() {
        return Context.tr("Make an offer");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return Context.tr("You must be logged to make an offer");
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateOfferForm(loggedUser));

        layout.addRight(new SideBarFeatureBlock(feature));
        layout.addRight(new SideBarDocumentationBlock("cc_by"));
        layout.addRight(new SidebarMarkdownHelp());

        return layout;
    }

    private HtmlTitleBlock generateOfferForm(final Member loggedUser) {
        final HtmlTitleBlock offerPageContainer = new HtmlTitleBlock(Context.tr("Make an offer"), 1);

        if (offer != null) {
            offerPageContainer.add(new OfferBlock(offer, true));
        }

        // Create offer form
        final OfferActionUrl targetUrl = new OfferActionUrl(getSession().getShortKey(), feature);
        targetUrl.setDraftOffer(offer);
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        final FormBuilder ftool = new FormBuilder(OfferAction.class, targetUrl);

        form.addLanguageChooser(targetUrl.getLocaleParameter().getName(), Context.getLocalizator().getLanguageCode());
        form.addAsTeamField(new AsTeamField(targetUrl,
                                            loggedUser,
                                            UserTeamRight.TALK,
                                            Context.tr("In the name of "),
                                            Context.tr("Write this offer in the name of a team, and offer the contributions to this team.")));

        // Price field
        ftool.add(form, new HtmlMoneyField(targetUrl.getPriceParameter().getName()));

        // Date field
        ftool.add(form, new HtmlDateField(targetUrl.getExpiryDateParameter().getName(), Context.getLocalizator().getLocale()));

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
        final HtmlTextArea descriptionInput = new HtmlTextArea(targetUrl.getDescriptionParameter().getName(), 10, 80);
        ftool.add(form, descriptionInput);
        ftool.setDefaultValueIfNeeded(descriptionInput, suggestedValue);

        // license
        if (this.offer == null) {
            final HtmlDropDown licenseInput = new HtmlDropDown(targetUrl.getLicenseParameter().getName());
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
            ftool.add(form, licenseInput);
            licenseInput.setComment(new HtmlMixedText(Context.tr("Licenses must be <0::OSI-approved>. You should use one of the listed licenses to avoid <1::license proliferation>."),
                                                      new UrlString("http://opensource.org/licenses").getHtmlLink(),
                                                      new UrlString(Context.tr("http://en.wikipedia.org/wiki/License_proliferation")).getHtmlLink()));
        } else {
            form.add(new HtmlHidden(targetUrl.getLicenseParameter().getName(), offer.getlicense()));
        }

        final HtmlDiv validationDetails = new HtmlDiv();
        final HtmlParagraph showHideLink = new HtmlParagraph(Context.tr("Show validation details"));
        showHideLink.setCssClass("fake_link");

        final JsShowHide showHideValidationDetails = new JsShowHide(form, ftool.suggestedValueChanged(targetUrl.getPercentMajorParameter().getName())
                || ftool.suggestedValueChanged(targetUrl.getPercentFatalParameter().getName())
                || ftool.suggestedValueChanged(targetUrl.getDaysBeforeValidationParameter().getName()));
        showHideValidationDetails.setHasFallback(false);
        showHideValidationDetails.addActuator(showHideLink);
        showHideValidationDetails.addListener(validationDetails);
        // form.add(showHideLink);
        // form.add(validationDetails);
        showHideValidationDetails.apply();

        // days before validation
        ftool.add(validationDetails, new HtmlTextField(targetUrl.getDaysBeforeValidationParameter().getName()));

        // percent Fatal
        ftool.add(validationDetails, new HtmlTextField(targetUrl.getPercentFatalParameter().getName()));

        // percent Major
        ftool.add(validationDetails, new HtmlTextField(targetUrl.getPercentMajorParameter().getName()));

        form.addSubmit(new HtmlSubmit(Context.tr("Add another milestone")));
        form.addSubmit(new HtmlSubmit(Context.tr("Finish your Offer")).setName(targetUrl.getIsFinishedParameter().getName()));

        offerPageContainer.add(form);

        return offerPageContainer;
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return MakeOfferPage.generateBreadcrumb(feature);
    }

    private static Breadcrumb generateBreadcrumb(final Feature feature) {
        final Breadcrumb breadcrumb = FeaturePage.generateBreadcrumbContributions(feature);

        breadcrumb.pushLink(new MakeOfferPageUrl(feature).getHtmlLink(tr("Make an offer")));

        return breadcrumb;
    }

}
