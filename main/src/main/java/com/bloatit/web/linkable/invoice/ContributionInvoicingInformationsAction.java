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

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ContributionInvoice;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.MilestoneContributionAmount;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.ContributionInvoicingInformationsActionUrl;

/**
 * Class that will create a new offer based on data received from a form.
 */
@ParamContainer("action/invoicing/contribution_invoicing_informations")
public final class ContributionInvoicingInformationsAction extends LoggedAction {

    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final ContributionInvoicingProcess process;

    private final ContributionInvoicingInformationsActionUrl url;

    public ContributionInvoicingInformationsAction(final ContributionInvoicingInformationsActionUrl url) {
        super(url);
        this.url = url;
        this.process = url.getProcess();
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        // TODO: do the job
        final PageIterable<MilestoneContributionAmount> contributionAmounts = process.getMilestone().getContributionAmounts();

        for (final MilestoneContributionAmount contributionAmount : contributionAmounts) {
            try {
                
                
                
                new ContributionInvoice(process.getActor(),
                                        contributionAmount.getContribution().getAuthor(),
                                        "Contribution",
                                        "Contribution",
                                        contributionAmount.getAmount(),
                                        contributionAmount.getMilestone(),
                                        contributionAmount.getContribution());
            } catch (final UnauthorizedOperationException e) {
                throw new BadProgrammerException("Fail create a ContributionInvoice", e);
            }

            // TODO taxes
        }

        return process.close();
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
        return Context.tr("You must be logged to generate invoices.");
    }

    @Override
    protected void transmitParameters() {
    }

}
