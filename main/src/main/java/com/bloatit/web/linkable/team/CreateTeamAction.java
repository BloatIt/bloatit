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
package com.bloatit.web.linkable.team;

import com.bloatit.data.DaoTeam.Right;
import com.bloatit.framework.webprocessor.annotations.MaxConstraint;
import com.bloatit.framework.webprocessor.annotations.MinConstraint;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
import com.bloatit.model.managers.TeamManager;
import com.bloatit.web.actions.LoggedAction;
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;

/**
 * <p>
 * An action used to create a new team
 * </p>
 */
@ParamContainer("team/docreate")
public final class CreateTeamAction extends LoggedAction {

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a team name"))
    @MinConstraint(min = 2,
                   message = @tr("The team unique name size has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 50,
                   message = @tr("The team unique name size has to be inferior to %constraint% your text is %valueLength% characters long."))
    private final String login;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a specification"))
    @MinConstraint(min = 4, message = @tr("The contact size has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 300, message = @tr("The contact size has to be inferior to %constraint%."))
    @Optional
    private final String contact;

    @RequestParam(role = Role.POST)
    @NonOptional(@tr("You forgot to write a description"))
    @MinConstraint(min = 4, message = @tr("The description size has to be superior to %constraint% but your text is %valueLength% characters long."))
    @MaxConstraint(max = 5000,
                   message = @tr("The description size has to be inferior to %constraint% but your text is %valueLength% characters long."))
    private final String description;

    @RequestParam(role = Role.POST)
    private final Right right;

    private final CreateTeamActionUrl url;

    public CreateTeamAction(final CreateTeamActionUrl url) {
        super(url);
        this.url = url;
        this.contact = url.getContact();
        this.description = url.getDescription();
        this.login = url.getLogin();
        this.right = url.getRight();
    }

    @Override
    protected Url checkRightsAndEverything(final Member me) {

        if (TeamManager.exist(login)) {
            session.notifyError(Context.tr("The team name ''{0}''already used. Find another name.", login));
            url.getLoginParameter().addErrorMessage(Context.tr("Team name already used."));
            return doProcessErrors();
        }

        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {

        final Team newTeam = new Team(login, contact, description, right, me);
        return new TeamPageUrl(newTeam);
    }

    @Override
    protected Url doProcessErrors(final ElveosUserToken userToken) {
        return new CreateTeamPageUrl();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a new team");
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getContactParameter());
        session.addParameter(url.getDescriptionParameter());
        session.addParameter(url.getLoginParameter());
        session.addParameter(url.getRightParameter());
    }
}
