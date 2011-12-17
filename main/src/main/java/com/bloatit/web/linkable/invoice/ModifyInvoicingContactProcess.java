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
package com.bloatit.web.linkable.invoice;

import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.ParamContainer.Protocol;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.MemberManager;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.linkable.master.WebProcess;
import com.bloatit.web.url.ModifyContactPageUrl;
import com.bloatit.web.url.ModifyInvoicingContactProcessUrl;

@ParamContainer(value = "invoicing/process", protocol = Protocol.HTTPS)
public class ModifyInvoicingContactProcess extends WebProcess {

    @RequestParam
    private Actor<?> actor;

    private final ModifyInvoicingContactProcessUrl url;

    @SuppressWarnings("unused")
    @RequestParam(message = @tr("The process is closed, expired, missing or invalid."))
    @NonOptional(@tr("The process is closed, expired, missing or invalid."))
    private final WebProcess parentProcess;

    @RequestParam()
    @Optional(value = "false")
    private final Boolean needAllInfos;

    public ModifyInvoicingContactProcess(final ModifyInvoicingContactProcessUrl url) {
        super(url);
        this.url = url;
        setActor(url.getActor());
        parentProcess = url.getParentProcess();
        needAllInfos = url.getNeedAllInfos();
    }

    @Override
    protected synchronized Url doProcess() {
        url.getParentProcess().addChildProcess(this);
        return new ModifyContactPageUrl(this);
    }

    @Override
    protected synchronized Url doProcessErrors() {
        return session.getLastVisitedPage();
    }

    @Override
    public synchronized void doLoad() {
        if (getActor() instanceof Member) {
            setActor(MemberManager.getById(getActor().getId()));
        } else if (getActor() instanceof Team) {
            setActor(TeamManager.getById(getActor().getId()));
        }
    }

    public Actor<?> getActor() {
        return actor;
    }

    public void setActor(final Actor<?> actor) {
        this.actor = actor;
    }

    public Boolean getNeedAllInfos() {
        return needAllInfos;
    }

}
