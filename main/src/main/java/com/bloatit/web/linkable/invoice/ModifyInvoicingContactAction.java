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

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.LengthConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.components.form.FormComment;
import com.bloatit.framework.webprocessor.components.form.FormField;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.framework.webprocessor.url.UrlParameter;
import com.bloatit.model.Member;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.linkable.master.LoggedElveosAction;
import com.bloatit.web.url.ModifyContactPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactActionUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("action/invoicingcontact/choose")
public final class ModifyInvoicingContactAction extends LoggedElveosAction {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ModifyInvoicingContactProcess process;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You have to specify a name."))
    @FormField(label = @tr("Name"))
    @FormComment(@tr("Your full name, or the name of your organization."))
    private final String name;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You have to specify a street."))
    @FormField(label = @tr("Street"), isShort = false)
    private final String street;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You have to specify a ZIP code."))
    @FormField(label = @tr("ZIP code"))
    private final String postalCode;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You have to specify a city."))
    @FormField(label = @tr("City"))
    private final String city;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("Extras"))
    private final String extras;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("VAT identification number"))
    private final String taxIdentification;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("I represent a company"))
    private final Boolean isCompany;

    @RequestParam(role = Role.POST)
    @LengthConstraint(length = 2, message = @tr("The country code must be %constraint% length."))
    @NonOptional(@tr("You have to specify a country."))
    @FormField(label = @tr("Country"))
    private final String country;

    // Specific informations
    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("Invoice ID template"))
    @FormComment(@tr("Format of the generated invoice numbers. See the side documentation for available fields. Example:&nbsp;'ELVEOS-{YEAR|4}{MONTH}{DAY}-F{ID|4}'"))
    private final String invoiceIdTemplate;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("Next Invoice ID number"))
    @FormComment(@tr("ID that will be used for the next generated invoice."))
    private final BigDecimal invoiceIdNumber;

    @RequestParam(role = Role.POST)
    @Optional
    @FormField(label = @tr("Legal identification"))
    private final String legalId;

    @RequestParam(role = Role.POST)
    @Optional
    private final BigDecimal taxRate;

    private final ModifyInvoicingContactActionUrl url;

    public ModifyInvoicingContactAction(final ModifyInvoicingContactActionUrl url) {
        super(url);
        this.url = url;
        this.process = url.getProcess();
        this.name = url.getName();
        this.street = url.getStreet();
        this.postalCode = url.getPostalCode();
        this.city = url.getCity();
        this.country = url.getCountry();
        this.extras = url.getExtras();
        this.taxIdentification = url.getTaxIdentification();
        this.isCompany = (url.getIsCompany() == null ? false : url.getIsCompany());

        // Specific informations
        this.invoiceIdTemplate = url.getInvoiceIdTemplate();
        this.invoiceIdNumber = url.getInvoiceIdNumber();
        this.legalId = url.getLegalId();

        this.taxRate = url.getTaxRate();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        try {
            process.getActor().getContact().setName(name);
            process.getActor().getContact().setStreet(street);
            process.getActor().getContact().setExtras(extras);
            process.getActor().getContact().setPostalCode(postalCode);
            process.getActor().getContact().setCity(city);
            process.getActor().getContact().setCountry(country);
            process.getActor().getContact().setTaxIdentification(taxIdentification);
            process.getActor().getContact().setIsCompany(isCompany);

            if (process.getNeedAllInfos()) {
                process.getActor().getContact().setInvoiceIdTemplate(invoiceIdTemplate);
                process.getActor().getContact().setInvoiceIdNumber(invoiceIdNumber.setScale(0, BigDecimal.ROUND_HALF_EVEN));
                process.getActor().getContact().setLegalId(legalId);
                process.getActor().getContact().setTaxRate(taxRate.divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_EVEN));
            }

        } catch (final UnauthorizedPrivateAccessException e) {
            throw new BadProgrammerException("Fail to update a invoicing contact of a member", e);
        }

        return process.close();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        boolean isOk = true;

        if (process.getNeedAllInfos()) {
            isOk &= checkOptional(this.invoiceIdTemplate, Context.tr("You must add an invoice number template."), url.getInvoiceIdTemplateParameter());
            isOk &= checkOptional(this.invoiceIdNumber, Context.tr("You must add an invoice No initial value."), url.getInvoiceIdNumberParameter());
            isOk &= checkOptional(this.legalId, Context.tr("You must add a legal ID."), url.getLegalIdParameter());
            isOk &= checkOptional(this.taxRate, Context.tr("You must add a tax rate."), url.getTaxRateParameter());

            if (this.invoiceIdTemplate != null) {
                final Pattern pattern = Pattern.compile("^(.*)(\\{ID(\\|([0-9]+))?\\})(.*)");
                final Matcher matcher = pattern.matcher(this.invoiceIdTemplate);
                if (!matcher.matches()) {
                    final String errorText = Context.tr("You must indicate a ID field in the template.");
                    url.getInvoiceIdTemplateParameter().addErrorMessage(errorText);
                    session.notifyError(errorText);
                    isOk = false;
                }
            }
        }

        if (!isOk) {
            return new ModifyContactPageUrl(process);
        }
        return NO_ERROR;
    }

    private boolean checkOptional(final Object object, final String errorText, final UrlParameter<?, ?> parameter) {
        if (object == null) {
            parameter.addErrorMessage(errorText);
            session.notifyError(errorText);
            return false;
        }
        return true;
    }

    @Override
    protected Url doProcessErrors() {
        return new ModifyContactPageUrl(process);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to make an offer.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getNameParameter());
        session.addParameter(url.getStreetParameter());
        session.addParameter(url.getExtrasParameter());
        session.addParameter(url.getPostalCodeParameter());
        session.addParameter(url.getCityParameter());
        session.addParameter(url.getCountryParameter());

        if (process != null && process.getNeedAllInfos()) {
            session.addParameter(url.getInvoiceIdTemplateParameter());
            session.addParameter(url.getInvoiceIdNumberParameter());
            session.addParameter(url.getLegalIdParameter());
            session.addParameter(url.getTaxIdentificationParameter());
            session.addParameter(url.getTaxRateParameter());
        }
    }
}
