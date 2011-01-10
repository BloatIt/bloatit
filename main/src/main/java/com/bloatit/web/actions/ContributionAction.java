/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
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
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.AccountChargingPageUrl;
import com.bloatit.web.utils.url.ContributePageUrl;
import com.bloatit.web.utils.url.ContributionActionUrl;
import com.bloatit.web.utils.url.IdeaPageUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("action/contribute")
public final class ContributionAction extends LoggedAction {

	public static final String AMOUNT_CODE = "contributionAmount";
	public static final String COMMENT_CODE = "comment";
	public static final String TARGET_IDEA = "targetIdea";

	@RequestParam(name = TARGET_IDEA, level = Level.ERROR)
	private final Demand targetIdea;

	@RequestParam(name = COMMENT_CODE, role = Role.POST, level = Level.INFO)
	private final String comment;

	@RequestParam(name = AMOUNT_CODE, role = Role.POST)
	private final BigDecimal amount;

	private final ContributionActionUrl url;

	public ContributionAction(final ContributionActionUrl url) throws RedirectException {
		super(url);
		this.url = url;
		this.targetIdea = url.getTargetIdea();
		this.comment = url.getComment();
		this.amount = url.getAmount();
	}

	@Override
	public final Url doProcessRestricted() throws RedirectException {
		// Authentication
		targetIdea.authenticate(session.getAuthToken());

		try {
			if (targetIdea.canContribute()) {
				targetIdea.addContribution(amount, comment);
				session.notifyGood(Context.tr("Thanks you for crediting {0} on this idea", Context.getLocalizator().getCurrency(amount).getLocaleString()));

				return new IdeaPageUrl(targetIdea);
			} else {
				// Should never happen
				session.notifyBad(Context.tr("For obscure reasons, you are not allowed to contribute on this idea."));
				return new ContributePageUrl(targetIdea);
			}
		} catch (final NotEnoughMoneyException e) {
			session.notifyBad(Context.tr("You need to charge your account before you can contribute."));
			session.addParam(AMOUNT_CODE, amount);
			session.addParameter(COMMENT_CODE, comment);
			
			session.setTargetPage(this.url); 
			return new AccountChargingPageUrl();
		}
	}

	@Override
	protected Url doProcessErrors() throws RedirectException {
		session.notifyList(url.getMessages());
		if(comment != null){
			session.addParameter(COMMENT_CODE, comment);
		}
		if(amount != null){
			session.addParam(AMOUNT_CODE, amount);
		}

		return new ContributePageUrl(targetIdea);
	}

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to contribute.");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(COMMENT_CODE, comment);
        session.addParam(AMOUNT_CODE, amount);
    }
}
