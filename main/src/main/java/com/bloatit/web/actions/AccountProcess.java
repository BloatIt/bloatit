//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.web.actions;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Team;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.linkable.money.PaymentProcess;
import com.bloatit.web.url.AccountProcessUrl;

@ParamContainer("accountprocess")
public abstract class AccountProcess extends WebProcess {

    @RequestParam
    @Optional
    private Team team;

    private BigDecimal amountToPayBeforeComission = new BigDecimal("0");
    private BigDecimal accountChargingAmount = new BigDecimal("0");
    private boolean locked = false;

    public AccountProcess(final AccountProcessUrl url) {
        super(url);
        team = url.getTeam();
    }

    @Override
    protected final Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected final synchronized void notifyChildAdded(final WebProcess subProcess) {
        if (subProcess.getClass().equals(PaymentProcess.class)) {
            locked = true;
        }
    }

    public final synchronized BigDecimal getAmountToPayBeforeComission() {
        return amountToPayBeforeComission;
    }

    public final synchronized void setAmountToPayBeforeComission(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amountToPayBeforeComission = amount;
    }

    public final synchronized void setAmountToCharge(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.accountChargingAmount = amount;
    }

    public final synchronized BigDecimal getAccountChargingAmount() {
        return accountChargingAmount;
    }

    public final synchronized void setTeam(final Team team) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.team = team;
    }

    public final synchronized Team getTeam() {
        return team;
    }

    public final synchronized void setLock(final boolean islocked) {
        locked = islocked;
    }

    public final synchronized boolean isLocked() {
        return locked;
    }

    protected synchronized final void unlock() {
        locked = false;
    }

    @Override
    protected final void doLoad() {
        if (team != null) {
            team = TeamManager.getById(team.getId());
        }
        doDoLoad();
    }

    public abstract void doDoLoad();

}
