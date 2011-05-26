//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.linkable.invoice;

import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.InvoicingContact;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Team;
import com.bloatit.model.managers.InvoicingContactManager;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.actions.WebProcess;
import com.bloatit.web.url.ContributionInvoicingProcessUrl;
import com.bloatit.web.url.InvoicingContactProcessUrl;

@ParamContainer(value = "contribution_invoicing/process", protocol = Protocol.HTTPS)
public class ContributionInvoicingProcess extends WebProcess  {

    @RequestParam
    private Actor<?> actor;

    @RequestParam(conversionErrorMsg = @tr("The milestone to invoice is ."))
    @ParamConstraint(optionalErrorMsg = @tr("The process is closed, expired, missing or invalid."))
    private final Milestone milestone;

    private InvoicingContact invoicingContact = null;

    private final ContributionInvoicingProcessUrl url;


    private InvoicingContactProcess invoicingContactProcess = null;

    public ContributionInvoicingProcess(final ContributionInvoicingProcessUrl url) {
        super(url);
        this.url = url;
        this.actor = url.getActor();
        this.milestone = url.getMilestone();

    }

    public InvoicingContact getInvoicingContact() {
        return invoicingContact;
    }

    @Override
    protected synchronized Url doProcess(final ElveosUserToken userToken) {
        return new InvoicingContactProcessUrl(actor, this);
    }

    @Override
    protected synchronized Url doProcessErrors(final ElveosUserToken userToken) {
        return session.getLastVisitedPage();
    }

    @Override
    public synchronized void doLoad() {

        if (getActor() instanceof Member) {
            setActor(MemberManager.getById(getActor().getId()));
        } else if (getActor() instanceof Team) {
            setActor(TeamManager.getById(getActor().getId()));
        }
        if(invoicingContact != null) {
            invoicingContact = InvoicingContactManager.getById(invoicingContact.getId());
        }
    }

    public Actor<?> getActor() {
        return actor;
    }

    public void setActor(Actor<?> actor) {
        this.actor = actor;
    }

    public void setInvoicingContact(InvoicingContact invoicingContact) {
        this.invoicingContact = invoicingContact;
    }

    @Override
    public synchronized void addChildProcess(WebProcess child) {
        invoicingContactProcess = (InvoicingContactProcess) child;
    }

    public Milestone getMilestone() {
        return milestone;
    }



}
