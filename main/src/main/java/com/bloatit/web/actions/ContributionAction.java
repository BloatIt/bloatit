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

import java.math.BigDecimal;

import com.bloatit.framework.Demand;
import com.bloatit.model.exceptions.NotEnoughMoneyException;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;

@ParamContainer("contribute")
public class ContributionAction extends Action {

    public static final String AMOUNT_CODE = "bloatit_contribute";
    public static final String COMMENT_CODE = "bloatit_comment";
    public static final String TARGET_DEMAND_CODE = "bloatit_target_demand";
    public static final String IDEA_CODE = "idea";

    @RequestParam(name = TARGET_DEMAND_CODE, role = Role.POST)
    private Demand targetDemand;

    @RequestParam(name = IDEA_CODE, role = Role.POST)
    private Demand idea;

    @RequestParam(name = COMMENT_CODE, defaultValue = "")
    private String comment;

    @RequestParam(name = AMOUNT_CODE, role = Role.POST)
    private BigDecimal amount;

    public ContributionAction(final ContributionActionUrl url) throws RedirectException {
        super(url);
        this.targetDemand = url.getTargetDemand();
        this.idea = url.getIdea();
        this.comment = url.getComment();
        this.amount = url.getAmount();
        
        session.notifyList(url.getMessages());
    }

    @Override
    public String doProcess() throws RedirectException {
        // Authentication
        targetDemand.authenticate(session.getAuthToken());

        try {
            if (targetDemand.canContribute()) {
                targetDemand.addContribution(amount, comment);
                session.notifyGood(session.tr("Thanks you for crediting " + amount + " on this idea"));
                return new IdeaPageUrl(idea).toString();
            } else {
                // Should never happen
                session.notifyBad(session.tr("For obscure reasons, you are not allowed to contribute on this idea."));
                return new ContributePageUrl(idea).toString();
            }
        } catch (final NotEnoughMoneyException e) {
            session.notifyBad(session.tr("You have not enought money left."));
            // TODO add this parameters into ContributePage url.
            return new ContributePageUrl(idea).toString();

        }
    }
}
