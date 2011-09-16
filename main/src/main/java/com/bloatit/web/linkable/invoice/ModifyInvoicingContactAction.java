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

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
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
    @NonOptional(@tr("You must add a name."))
    private final String name;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a street."))
    private final String street;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a Postcode."))
    private final String postalCode;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a city."))
    private final String city;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You must add a country."))
    private final String country;

    @RequestParam(role = Role.POST)
    @Optional
    private final String extras;

    // Specific informations
    @RequestParam(role = Role.POST)
    @Optional()
    private final String invoiceIdTemplate;

    @RequestParam(role = Role.POST)
    @Optional()
    private final BigDecimal invoiceIdNumber;

    @RequestParam(role = Role.POST)
    @Optional()
    private final String legalId;

    @RequestParam(role = Role.POST)
    @Optional()
    private final String taxIdentification;

    @RequestParam(role = Role.POST)
    @Optional()
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

        // Specific informations
        this.invoiceIdTemplate = url.getInvoiceIdTemplate();
        this.invoiceIdNumber = url.getInvoiceIdNumber();
        this.legalId = url.getLegalId();
        this.taxIdentification = url.getTaxIdentification();
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

            if (process.getNeedAllInfos()) {
                process.getActor().getContact().setInvoiceIdTemplate(invoiceIdTemplate);
                process.getActor().getContact().setInvoiceIdNumber(invoiceIdNumber.setScale(0, BigDecimal.ROUND_HALF_EVEN));
                process.getActor().getContact().setLegalId(legalId);
                process.getActor().getContact().setTaxIdentification(taxIdentification);
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
            isOk &= checkOptional(this.taxIdentification, Context.tr("You must add a tax identification."), url.getTaxIdentificationParameter());
            isOk &= checkOptional(this.taxRate, Context.tr("You must add a tax rate."), url.getTaxRateParameter());
        }

        if (!isOk) {
            return new ModifyContactPageUrl(process);
        }
        return NO_ERROR;
    }

    private boolean checkOptional(Object object, String errorText, UrlParameter<?, ?> parameter) {
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

        if (process.getNeedAllInfos()) {
            session.addParameter(url.getInvoiceIdTemplateParameter());
            session.addParameter(url.getInvoiceIdNumberParameter());
            session.addParameter(url.getLegalIdParameter());
            session.addParameter(url.getTaxIdentificationParameter());
            session.addParameter(url.getTaxRateParameter());
        }
    }
}
