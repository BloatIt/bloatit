/*
 * Copyright (C) 2010 BloatIt.
 * 
 * This file is part of BloatIt.
 * 
 * BloatIt is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with
 * BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.web.actions;

import com.bloatit.framework.Demand;
import com.bloatit.framework.managers.DemandManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;
import com.bloatit.web.pages.ContributePage;
import com.bloatit.web.pages.demand.DemandPage;
import com.bloatit.web.pages.IndexPage;
import com.bloatit.web.server.Action;
import com.bloatit.web.server.Session;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ContributionAction extends Action {

    public ContributionAction(Session session, Map<String, String> parameters) {
        super(session, parameters);
    }

    public ContributionAction(Session session) {
        this(session, new HashMap<String, String>());
    }

    @Override
    public String getCode() {
        return "contribute";
    }

    /**
     * Use to determine an identifier to use for forms.
     */
    public String getContributionCode() {
        return "bloatit_contribute";
    }

    public String getCommentCode() {
        return "bloatit_comment";
    }

    @Override
    protected void process() {
        Demand targetDemand = null;
        String idea = null;
        String amountStr = null;
        String comment = "";
        BigDecimal amount = BigDecimal.ZERO;

        // Get parameters
        if (parameters.containsKey("idea")) {
            idea = parameters.get("idea");
            try {
                targetDemand = DemandManager.getDemandById(Integer.parseInt(idea));
            } catch (NumberFormatException nfe) {}
        }

        if (parameters.containsKey(this.getContributionCode())) {
            amountStr = parameters.get(getContributionCode());
            try {
                amount = new BigDecimal(amountStr);
            } catch (NumberFormatException nfe) {}
        }

        if (parameters.containsKey(this.getCommentCode())) {
            comment = this.parameters.get(this.getCommentCode());
        }

        // Check validity of values
        if (targetDemand == null) {
            htmlResult.setRedirect(new IndexPage(session));
            session.notifyBad(session.tr(idea + " is not a valid idea identifier"));
            return;
        }

        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            htmlResult.setRedirect(new ContributePage(session, parameters));
            session.notifyBad(session.tr("The amount " + amountStr + " is not a valid entry"));
            return;
        }

        if (amount.compareTo(new BigDecimal("10000000")) > 0) {
            htmlResult.setRedirect(new ContributePage(session, parameters));
            session.notifyBad(session.tr("Thank you for being so generous ... " + "but we can't accept such a big amount : " + amountStr));
            return;
        }

        // Authentication
        targetDemand.authenticate(session.getAuthToken());

        try {
            if (targetDemand.canContribute()) {
                targetDemand.addContribution(amount, comment);
                htmlResult.setRedirect(new DemandPage(session, targetDemand));
                session.notifyGood(session.tr("Thanks you for crediting " + amount + " on this idea"));
            } else {
                // Should never happen
                htmlResult.setRedirect(new ContributePage(session, parameters));
                session.notifyBad(session.tr("For obscure reasons, you are not allowed to contribute on this idea."));
                return;
            }
        } catch (NotEnoughMoneyException e) {
            htmlResult.setRedirect(new ContributePage(session, parameters));
            session.notifyBad(session.tr("You have not enought money left."));
        }
    }
}
