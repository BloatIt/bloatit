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
import com.bloatit.web.pages.DemandPage;
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
        BigDecimal amount = BigDecimal.ZERO;

        // Get parameters
        if (parameters.containsKey("idea")) {
            idea = parameters.get("idea");
            try {
                targetDemand = DemandManager.getDemandById(Integer.parseInt(idea));
            } catch (NumberFormatException nfe) {}
        }

        if (parameters.containsKey(getContributionCode())) {
            amountStr = parameters.get(getContributionCode());
            try {
                amount = new BigDecimal(amountStr);
            } catch (NumberFormatException nfe) {}
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

        // Authentication
        targetDemand.authenticate(session.getAuthToken());

        // Case everything OK
        try {
            targetDemand.addContribution(amount, "");
            htmlResult.setRedirect(new DemandPage(session, targetDemand));
            session.notifyGood(session.tr("You credited " + amount + " on the idea"));
        } catch (NotEnoughMoneyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
