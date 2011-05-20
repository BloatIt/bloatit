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
package com.bloatit.web.linkable.money;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.InvoicingContact;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.actions.PaymentProcess;
import com.bloatit.web.linkable.contribution.ContributionProcess;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CreateInvoicingContactActionUrl;
import com.bloatit.web.url.StaticAccountChargingPageUrl;
import com.bloatit.web.url.StaticCheckContributionPageUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("action/invoicingcontact/create")
public final class CreateInvoicingContactAction extends UserContentAction {

    @RequestParam(conversionErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final PaymentProcess process;


    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("You must add a name a invoicing information."),
                     min = "1", minErrorMsg = @tr("You must add a name a invoicing information."))
    private final String name;

    @RequestParam(role = Role.POST)
    @ParamConstraint(optionalErrorMsg = @tr("You must add an address a invoicing information."),
                     min = "1", minErrorMsg = @tr("You must add an address a invoicing information."))
    private final String address;


    private final CreateInvoicingContactActionUrl url;

    public CreateInvoicingContactAction(final CreateInvoicingContactActionUrl url) {
        super(url, UserTeamRight.TALK);
        this.url = url;
        this.process = url.getProcess();
        this.name = url.getName();
        this.address = url.getAddress();

    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {

        InvoicingContact contact = new InvoicingContact(name, address, getActor(me));

        process.setInvoicingContact(contact);

        if(process instanceof AccountChargingProcess) {
            return new StaticAccountChargingPageUrl((AccountChargingProcess) process);
        }

        if(process instanceof ContributionProcess) {
            return new StaticCheckContributionPageUrl((ContributionProcess) process);
        }


        return null;
    }

    private Actor<?> getActor(final Member member) {
        if (process.getTeam() != null) {
            return process.getTeam();
        }
        return member;
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to make an offer.");
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getNameParameter());
        session.addParameter(url.getAddressParameter());
    }

    @Override
    protected boolean verifyFile(final String filename) {
        return true;
    }
}
