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
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.RequestParam.Role;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;

@ParamContainer("action/contribute")
public class ContributionAction extends Action {

    public static final String AMOUNT_CODE = "contributionAmount";
    public static final String COMMENT_CODE = "comment";
    public static final String TARGET_IDEA = "targetIdea";

    @RequestParam(name = TARGET_IDEA, level = Level.ERROR)
    private Demand targetIdea;

    @RequestParam(name = COMMENT_CODE, role = Role.SESSION, level = Level.INFO)
    private String comment;

    @RequestParam(name = AMOUNT_CODE, role = Role.SESSION)
    private BigDecimal amount;

    private final ContributionActionUrl url;

    public ContributionAction(final ContributionActionUrl url) throws RedirectException {
        super(url);
        this.url = url;
        this.targetIdea = url.getTargetIdea();
        this.comment = url.getComment();
        this.amount = url.getAmount();

        session.notifyList(url.getMessages());
    }

    @Override
    public String doProcess() throws RedirectException {
        // Authentication
        targetIdea.authenticate(session.getAuthToken());

        try {
            if (targetIdea.canContribute()) {
                targetIdea.addContribution(amount, comment);
                session.notifyGood(session.tr("Thanks you for crediting " + amount + " on this idea"));

                return new IdeaPageUrl(targetIdea).urlString();
            } else {
                // Should never happen
                session.notifyBad(session.tr("For obscure reasons, you are not allowed to contribute on this idea."));
                return new ContributePageUrl(targetIdea).urlString();// TODO put
                                                                     // parameters
            }
        } catch (final NotEnoughMoneyException e) {
            session.notifyBad(session.tr("You need to charge your account before you can contribute."));
            session.addParam(AMOUNT_CODE, amount.toString());
            session.addParam(COMMENT_CODE, comment);
            // Sets the target page to here
            session.setTargetPage(this.url.urlString()); // Redirects to the
                                                         // account charging
                                                         // page
            // ToDO : give through session :
            // contributionPageUrl.addParameter(AccountChargingAction.CHARGE_AMOUNT_CODE,
            // amount);
            return new AccountChargingPageUrl().urlString();
        }
    }
}
