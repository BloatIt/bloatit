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
package com.bloatit.framework.webprocessor;

import java.math.BigDecimal;

import javax.mail.IllegalWriteException;

import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Team;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.linkable.money.PaylineProcess;
import com.bloatit.web.url.PaymentProcessUrl;

@ParamContainer("paymentprocess")
public abstract class PaymentProcess extends WebProcess {

    @RequestParam
    @Optional
    private Team team;

    private BigDecimal amountToPay = new BigDecimal("0");
    private BigDecimal amountToCharge = new BigDecimal("0");
    private boolean locked = false;

    public PaymentProcess(final PaymentProcessUrl url) {
        super(url);
        team = url.getTeam();
    }

    @Override
    protected final Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    protected final void notifyChildAdded(final WebProcess subProcess) {
        if (subProcess.getClass().equals(PaylineProcess.class)) {
            locked = true;
        }
    }

    public final BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public final void setAmountToPay(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amountToPay = amount;
    }

    public final void setAmountToCharge(final BigDecimal amount) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.amountToCharge = amount;
    }

    public final BigDecimal getAmountToCharge() {
        return amountToCharge;
    }

    public final void setTeam(final Team team) throws IllegalWriteException {
        if (locked) {
            throw new IllegalWriteException();
        }
        this.team = team;
    }

    public final Team getTeam() {
        return team;
    }

    public final void setLock(final boolean islocked) {
        locked = islocked;
    }

    public final boolean isLocked() {
        return locked;
    }
    
    protected final void unlock() {
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
