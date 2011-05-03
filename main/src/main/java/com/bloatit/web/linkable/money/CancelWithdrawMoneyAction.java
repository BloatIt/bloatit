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
package com.bloatit.web.linkable.money;

import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CancelWithdrawMoneyActionUrl;

@ParamContainer("money/docancelwithdraw")
public class CancelWithdrawMoneyAction extends LoggedAction {

    @RequestParam(role = Role.GET)
    private final MoneyWithdrawal moneyWithdrawal;

    private final CancelWithdrawMoneyActionUrl url;

    public CancelWithdrawMoneyAction(CancelWithdrawMoneyActionUrl url) {
        super(url);
        this.url = url;
        moneyWithdrawal = url.getMoneyWithdrawal();
    }

    @Override
    protected Url doCheckRightsAndEverything(Member me) {
        return NO_ERROR;
    }

    @Override
    protected Url doProcessRestricted(Member me) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Url doProcessErrors() {
        if(moneyWithdrawal != null) {

            /*if(moneyWithdrawal.getActor() instanceof Team) {
                new TeamPageUrl(moneyWithdrawal.getActor());
            }
            return new AccountPageUrl();*/

        }
        return null;

    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to cancel a withdrawal.");
    }

    @Override
    protected void transmitParameters() {
    }

}
