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
package com.bloatit.web.linkable.invoice;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.RedirectException;
import com.bloatit.framework.utils.i18n.Country;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.HtmlTitleBlock;
import com.bloatit.framework.webprocessor.components.form.FormBuilder;
import com.bloatit.framework.webprocessor.components.form.HtmlCheckbox;
import com.bloatit.framework.webprocessor.components.form.HtmlDropDown;
import com.bloatit.framework.webprocessor.components.form.HtmlFormField.LabelPosition;
import com.bloatit.framework.webprocessor.components.form.HtmlNumberField;
import com.bloatit.framework.webprocessor.components.form.HtmlPercentField;
import com.bloatit.framework.webprocessor.components.form.HtmlSubmit;
import com.bloatit.framework.webprocessor.components.form.HtmlTextField;
import com.bloatit.framework.webprocessor.components.javascript.HtmlHiddenableDiv;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Contact;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.components.HtmlElveosForm;
import com.bloatit.web.linkable.IndexPage;
import com.bloatit.web.linkable.contribution.CheckContributePage;
import com.bloatit.web.linkable.contribution.ContributionProcess;
import com.bloatit.web.linkable.documentation.SideBarDocumentationBlock;
import com.bloatit.web.linkable.master.Breadcrumb;
import com.bloatit.web.linkable.master.LoggedElveosPage;
import com.bloatit.web.linkable.master.sidebar.TwoColumnLayout;
import com.bloatit.web.linkable.money.AccountChargingPage;
import com.bloatit.web.linkable.money.AccountChargingProcess;
import com.bloatit.web.url.ModifyContactPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactActionUrl;

/**
 * A page used choose or create the invoicing contact to use for the comission
 * invoice.
 */
@ParamContainer(value = "account/charging/invoicing_contact", protocol = Protocol.HTTPS)
public final class ModifyContactPage extends LoggedElveosPage {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ModifyInvoicingContactProcess process;

    private final ModifyContactPageUrl url;

    public ModifyContactPage(final ModifyContactPageUrl url) {
        super(url);
        this.url = url;
        process = url.getProcess();
    }

    @Override
    public HtmlElement createBodyContentOnParameterError() throws RedirectException {
        if (url.getMessages().hasMessage()) {
            throw new RedirectException(Context.getSession().pickPreferredPage());
        }
        return createBodyContent();
    }

    @Override
    public HtmlElement createRestrictedContent(final Member loggedUser) throws RedirectException {
        final TwoColumnLayout layout = new TwoColumnLayout(true, url);
        layout.addLeft(generateInvoicingContactForm(loggedUser));

        if (process.getNeedAllInfos()) {
            layout.addRight(new SideBarDocumentationBlock("invoice_id_template"));
        }

        return layout;
    }

    private HtmlElement generateInvoicingContactForm(final Member member) {
        final HtmlTitleBlock group;
        if (process.getActor().isTeam()) {
            group = new HtmlTitleBlock(tr("Invoicing informations of {0}", process.getActor().getDisplayName()), 1);
        } else {
            group = new HtmlTitleBlock(tr("Your invoicing informations"), 1);
        }

        group.add(generateCommonInvoicingContactForm(member));

        return group;
    }

    private HtmlElement generateCommonInvoicingContactForm(final Member member) {

        // Create contact form
        final ModifyInvoicingContactActionUrl targetUrl = new ModifyInvoicingContactActionUrl(getSession().getShortKey(), process);
        final HtmlElveosForm form = new HtmlElveosForm(targetUrl.urlString());
        FormBuilder ftool = new FormBuilder(ModifyInvoicingContactAction.class, targetUrl);

        try {
            Contact contact = member.getContact();

            HtmlTextField name = new HtmlTextField(targetUrl.getNameParameter().getName());
            ftool.add(form, name);
            ftool.setDefaultValueIfNeeded(name, contact.getName());

            HtmlCheckbox isCompanyCheckbox = new HtmlCheckbox(targetUrl.getIsCompanyParameter().getName(), LabelPosition.BEFORE);
            ftool.add(form, isCompanyCheckbox);
            ftool.setDefaultValueIfNeeded(isCompanyCheckbox, String.valueOf(contact.isCompany()));

            HtmlTextField street = new HtmlTextField(targetUrl.getStreetParameter().getName());
            ftool.add(form, street);
            ftool.setDefaultValueIfNeeded(street, contact.getStreet());

            HtmlTextField extras = new HtmlTextField(targetUrl.getExtrasParameter().getName());
            ftool.add(form, extras);
            ftool.setDefaultValueIfNeeded(extras, contact.getExtras());

            // Country
            final HtmlDropDown country = new HtmlDropDown(targetUrl.getCountryParameter().getName());
            for (final Country entry : Country.getAvailableCountries()) {
                country.addDropDownElement(entry.getCode(), entry.getName());
            }
            if (contact.getCountry() != null) {
                ftool.setDefaultValueIfNeeded(country, contact.getCountry());
            } else {
                ftool.setDefaultValueIfNeeded(country, Context.getLocalizator().getCountryCode());
            }
            ftool.add(form, country);

            HtmlTextField city = new HtmlTextField(targetUrl.getCityParameter().getName());
            ftool.add(form, city);
            ftool.setDefaultValueIfNeeded(city, contact.getCity());

            HtmlTextField zipcode = new HtmlTextField(targetUrl.getPostalCodeParameter().getName());
            ftool.add(form, zipcode);
            ftool.setDefaultValueIfNeeded(zipcode, contact.getPostalCode());

            HtmlHiddenableDiv hiddenableDiv = new HtmlHiddenableDiv(isCompanyCheckbox, contact.isCompany());
            form.add(hiddenableDiv);
            ftool.add(hiddenableDiv, new HtmlTextField(targetUrl.getTaxIdentificationParameter().getName()));

            if (process.getNeedAllInfos()) {
                final HtmlTitleBlock specificForm = new HtmlTitleBlock(Context.tr("Invoice issuing"), 1);
                form.add(specificForm);

                // Invoice ID Number
                BigDecimal invoiceIdNumberValue = contact.getInvoiceIdNumber();
                String invoiceIdNumberText = null;

                if (invoiceIdNumberValue != null) {
                    invoiceIdNumberText = String.valueOf(invoiceIdNumberValue.intValue());
                }

                HtmlNumberField invoiceIdNumber = new HtmlNumberField(targetUrl.getInvoiceIdNumberParameter().getName());
                ftool.add(form, invoiceIdNumber);
                ftool.setDefaultValueIfNeeded(invoiceIdNumber, invoiceIdNumberText);

                HtmlTextField invoiceIdTemplate = new HtmlTextField(targetUrl.getInvoiceIdTemplateParameter().getName());
                ftool.add(form, invoiceIdTemplate);
                ftool.setDefaultValueIfNeeded(invoiceIdTemplate, contact.getInvoiceIdTemplate());

                HtmlTextField legalId = new HtmlTextField(targetUrl.getLegalIdParameter().getName());
                ftool.add(form, legalId);
                ftool.setDefaultValueIfNeeded(legalId, contact.getLegalId());

                HtmlPercentField taxeRate = new HtmlPercentField(targetUrl.getTaxRateParameter().getName());
                ftool.add(form, taxeRate);
                BigDecimal taxRate = contact.getTaxRate();
                if (taxRate != null) {
                    ftool.setDefaultValueIfNeeded(taxeRate, taxRate.multiply(new BigDecimal("100")).toPlainString());
                }
            }
            form.addSubmit(new HtmlSubmit(Context.tr("Update invoicing contact")));
        } catch (final UnauthorizedPrivateAccessException e) {
            throw new ShallNotPassException("The user is not allowed to access to his contact informations");
        }

        return form;
    }


    @Override
    protected String createPageTitle() {
        return tr("Invoicing informations");
    }

    @Override
    public boolean isStable() {
        return false;
    }

    @Override
    public String getRefusalReason() {
        return tr("You must be logged to add invoicing informations");
    }

    @Override
    protected Breadcrumb createBreadcrumb(final Member member) {
        return generateBreadcrumb(member, (process.getActor().isTeam() ? (Team) process.getActor() : null), process);
    }

    protected static Breadcrumb generateBreadcrumb(final Member member, final Team asTeam, final ModifyInvoicingContactProcess process) {
        final Breadcrumb breadcrumb;

        if (process.getFather() instanceof AccountChargingProcess) {
            breadcrumb = AccountChargingPage.generateBreadcrumb(member, asTeam, (AccountChargingProcess) process.getFather());
        } else if (process.getFather() instanceof ContributionProcess) {
            final ContributionProcess process2 = (ContributionProcess) process.getFather();
            breadcrumb = CheckContributePage.generateBreadcrumb(process2.getFeature(), process2);
        } else {
            breadcrumb = IndexPage.generateBreadcrumb();
        }

        final Url url = new ModifyContactPageUrl(process);

        breadcrumb.pushLink(url.getHtmlLink(tr("Invoicing contact")));
        return breadcrumb;
    }

}
