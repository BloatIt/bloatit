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
package com.bloatit.web.linkable.contribution;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.web.linkable.usercontent.UserContentAction;
import com.bloatit.web.url.CheckContributionActionUrl;
import com.bloatit.web.url.CheckContributionPageUrl;
import com.bloatit.web.url.ContributePageUrl;

/**
 * A response to a form used to create a contribution to a feature
 */
@ParamContainer("action/contribute/check")
public final class CheckContributionAction extends UserContentAction {

    @RequestParam
    private final ContributionProcess process;

    @RequestParam(role = Role.POST)
    @ParamConstraint(max = "140", maxErrorMsg = @tr("Your comment is too long. It must be less than 140 char long."))
    @Optional
    private final String comment;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "0", minIsExclusive = true, minErrorMsg = @tr("Amount must be superior to 0."),//
                     max = "1000000000", maxErrorMsg = @tr("We cannot accept such a generous offer!"),//
                     precision = 0, precisionErrorMsg = @tr("Please do not use Cents."), optionalErrorMsg = @tr("You must indicate an amount."))
    private final BigDecimal amount;

    private final CheckContributionActionUrl url;

    public CheckContributionAction(final CheckContributionActionUrl url) {
        super(url, UserTeamRight.BANK);
        this.url = url;
        this.process = url.getProcess();
        this.comment = url.getComment();
        this.amount = url.getAmount();
    }

    @Override
    public Url doDoProcessRestricted(final Member me, final Team asTeam) {
        final CheckContributionPageUrl checkContributionPageUrl = new CheckContributionPageUrl(process);

        try {
            if (!process.getAmount().equals(amount)) {
                process.setAmount(amount);
            }
            if (!(process.getComment() == comment || (process.getComment() != null && process.getComment().equals(comment)))) {
                process.setComment(comment);
            }
            if (!(process.getTeam() == asTeam || (process.getTeam() != null && process.getTeam().equals(asTeam)))) {
                process.setTeam(asTeam);
            }
        } catch (final IllegalWriteException e) {
            session.notifyBad(tr("The contribution's amount is locked during the payment process."));
        }

        return checkContributionPageUrl;
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    protected boolean verifyFile(final String filename) {
        // no file
        return true;
    }

    @Override
    protected Url doProcessErrors(ElveosUserToken authToken) {
        return new ContributePageUrl(process);
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to contribute.");
    }

    @Override
    protected void doTransmitParameters() {
        session.addParameter(url.getCommentParameter());
        session.addParameter(url.getAmountParameter());
    }

}
