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
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.AccountChargingPage;
import com.bloatit.web.html.pages.ContributePage;
import com.bloatit.web.html.pages.idea.IdeaPage;
import com.bloatit.web.utils.url.Request;
import com.bloatit.web.utils.url.UrlBuilder;

public class ContributionAction extends Action {

    public static final String AMOUNT_CODE = "contributionAmount";
    public static final String COMMENT_CODE = "comment";
    public static final String TARGET_IDEA = "targetIdea";

    @RequestParam(name = TARGET_IDEA)
    private Demand targetIdea;

    @RequestParam(name = COMMENT_CODE, defaultValue = "no comment", role = RequestParam.Role.POST)
    private String comment;
    
    @RequestParam(name = AMOUNT_CODE, role = RequestParam.Role.POST)
    private BigDecimal amount;

    public ContributionAction(final Request request) throws RedirectException {
        super(request);
        request.setValues(this);
        session.notifyList(request.getMessages());
    }

    @Override
    public String process() throws RedirectException {
        UrlBuilder contributionPageUrl = new UrlBuilder(ContributePage.class);
        contributionPageUrl.addParameter(TARGET_IDEA, targetIdea);
        contributionPageUrl.addParameter(COMMENT_CODE, comment);
        contributionPageUrl.addParameter(AMOUNT_CODE, amount);

        if (request.getMessages().hasMessage(Level.ERROR)) {
            // TODO specific si idea not found
            throw new RedirectException(contributionPageUrl.buildUrl());
        }

        // Authentication
        targetIdea.authenticate(session.getAuthToken());

        try {
            if (targetIdea.canContribute()) {
                targetIdea.addContribution(amount, comment);
                session.notifyGood(session.tr("Thanks you for crediting " + amount + " on this idea"));
                return new UrlBuilder(IdeaPage.class).addParameter("idea", targetIdea).buildUrl();
            } else {
                // Should never happen
                session.notifyBad(session.tr("For obscure reasons, you are not allowed to contribute on this idea."));
                return contributionPageUrl.buildUrl();
            }
        } catch (final NotEnoughMoneyException e) {
            session.notifyBad(session.tr("You need to charge your account before you can contribute."));

            // Sets the target page to here
            UrlBuilder contributionActionUrl = new UrlBuilder(ContributionAction.class, this.request.getParameters());
            session.setTargetPage(contributionActionUrl.buildUrl());

            // Redirects to the account charging page
            UrlBuilder accountCharging = new UrlBuilder(AccountChargingPage.class);
            // ToDO : give through session : contributionPageUrl.addParameter(AccountChargingAction.CHARGE_AMOUNT_CODE, amount);

            return accountCharging.buildUrl();
        }
    }
}
