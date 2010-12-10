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
package test.actions;

import com.bloatit.framework.Demand;
import com.bloatit.model.exceptions.NotEnoughMoneyException;
import com.bloatit.web.utils.Message.Level;
import com.bloatit.web.utils.RequestParam;
import test.Action;
import java.math.BigDecimal;
import test.RedirectException;
import test.Request;
import test.UrlBuilder;
import test.pages.ContributePage;
import test.pages.demand.DemandPage;

public class ContributionAction extends Action {

    public static final String AMOUNT_CODE = "bloatit_contribute";
    public static final String COMMENT_CODE = "bloatit_comment";
    public static final String TARGET_DEMAND_CODE = "bloatit_target_demand";
    public static final String IDEA_CODE = "idea";

    @RequestParam(name=TARGET_DEMAND_CODE)
    private Demand targetDemand;

    @RequestParam(name=IDEA_CODE)
    private Demand idea ;

    @RequestParam(name=COMMENT_CODE, defaultValue="no comment")
    private String comment;

    @RequestParam(name=AMOUNT_CODE)
    private BigDecimal amount;

    public ContributionAction(Request request) throws RedirectException {
        super(request);
        request.setValues(this);
        session.notifyList(request.getMessages());
        if (request.getMessages().hasMessage(Level.ERROR)){
            // TODO specific si idea not found
            throw new RedirectException(new UrlBuilder(ContributePage.class).buildUrl());
        }
    }

    @Override
    public String process() {
        // Authentication
        targetDemand.authenticate(session.getAuthToken());

        try {
            if (targetDemand.canContribute()) {
                targetDemand.addContribution(amount, comment);
                session.notifyGood(session.tr("Thanks you for crediting " + amount + " on this idea"));
                return new UrlBuilder(DemandPage.class).addParameter("idea", idea).buildUrl();
            } else {
                // Should never happen
                session.notifyBad(session.tr("For obscure reasons, you are not allowed to contribute on this idea."));
                return new UrlBuilder(ContributePage.class).buildUrl();
            }
        } catch (NotEnoughMoneyException e) {
            session.notifyBad(session.tr("You have not enought money left."));
            // TODO add this parameters into ContributePage url.
            return new UrlBuilder(ContributePage.class).buildUrl();
            
        }
    }
}
