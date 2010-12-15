/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.framework.ExternalAccount;
import com.bloatit.framework.Member;
import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.html.pages.IndexPage;
import com.bloatit.web.utils.url.LoginActionUrl;
import com.bloatit.web.utils.url.UrlBuilder;
import java.math.BigDecimal;

        @ParamContainer("account/charging")

public class AccountChargingAction extends Action {

    public final static String CHARGE_AMOUNT_CODE = "chargeAmount";
    
    @RequestParam(level = Level.ERROR, name = CHARGE_AMOUNT_CODE, role = RequestParam.Role.POST)
    BigDecimal amount;
    private final LoginActionUrl url;

    public AccountChargingAction(LoginActionUrl url) throws RedirectException {
        super(url);
        this.url = url;
    }

    @Override
    protected String doProcess() throws RedirectException {
        if (url.getMessages().hasMessage(Level.ERROR)) {
            // TODO
            throw new RedirectException(new UrlBuilder(IndexPage.class).buildUrl());
        }
        Member targetMember = session.getAuthToken().getMember();

        if( !targetMember.canGetInternalAccount() ){
            session.notifyError(session.tr("Your current rights do not allow you to charge money"));
            return new UrlBuilder(IndexPage.class).buildUrl();
        }

        targetMember.authenticate(session.getAuthToken());
        targetMember.getInternalAccount().authenticate(session.getAuthToken());

        ExternalAccount account = new ExternalAccount(targetMember, AccountType.IBAN, "plop");
        targetMember.getInternalAccount().chargeAmount(amount, account);

        return session.getPreferredPage();
    }
}
