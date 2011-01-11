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

import com.bloatit.framework.ExternalAccount;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.AccountChargingActionUrl;
import com.bloatit.web.utils.url.IndexPageUrl;
import com.bloatit.web.utils.url.LoginPageUrl;
import com.bloatit.web.utils.url.Url;

@ParamContainer("action/account/charging")
public class AccountChargingAction extends LoggedAction {

    public final static String CHARGE_AMOUNT_CODE = "chargeAmount";

    @RequestParam(level = Level.ERROR, name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.POST)
    BigDecimal amount;

    private final Url url;

    public AccountChargingAction(final AccountChargingActionUrl url) {
        super(url);
        this.url = url;
        this.amount = url.getAmount();
    }

    @Override
	public final Url doProcessRestricted() throws RedirectException {
        if (url.getMessages().hasMessage(Level.ERROR)) {
            session.notifyList(url.getMessages());
            throw new RedirectException(new IndexPageUrl());
        }
        final Member targetMember = session.getAuthToken().getMember();

        targetMember.authenticate(session.getAuthToken());
        targetMember.getInternalAccount().authenticate(session.getAuthToken());

        final ExternalAccount account = new ExternalAccount(targetMember, AccountType.IBAN, "plop");
        account.authenticate(session.getAuthToken());
        targetMember.getInternalAccount().chargeAmount(amount, account);

        if (!targetMember.canGetInternalAccount()) {
            session.notifyError(Context.tr("Your current rights do not allow you to charge money"));
            return new IndexPageUrl();
        }

        return session.pickPreferredPage();
    }

    @Override
	protected final Url doProcessErrors() throws RedirectException {
    	// TODO
		return new LoginPageUrl();
    }

	@Override
	protected String getRefusalReason() {
		return Context.tr("You must be logged to charge your account.");
	}

	@Override
	protected void transmitParameters() {
		session.addParameter(CHARGE_AMOUNT_CODE, amount.toPlainString());
	}
}
