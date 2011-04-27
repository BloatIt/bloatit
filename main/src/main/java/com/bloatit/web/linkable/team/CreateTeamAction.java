package com.bloatit.web.linkable.team;

import com.bloatit.data.DaoTeam.Right;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Member;
import com.bloatit.model.Team;
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
    protected final static String PROTECTED = "PROTECTED";
    protected final static String PUBLIC = "PUBLIC";

    @RequestParam(role = Role.POST)
    @ParamConstraint(
                     min = "4",
                     minErrorMsg = @tr("The team unique name size has to be superior to %constraint% but your text is %valueLength% characters long."),//
                     max = "50",
                     maxErrorMsg = @tr("The team unique name size has to be inferior to %constraint% your text is %valueLength% characters long."),//
                     optionalErrorMsg = @tr("You forgot to write a team name"))
    private final String login;

    @RequestParam(role = Role.POST)
    @ParamConstraint(
                     min = "4",
                     minErrorMsg = @tr("The team display name size has to be superior to %constraint% but your text is %valueLength% characters long."),//
                     max = "50",
                     maxErrorMsg = @tr("The team display name size has to be inferior to %constraint% your text is %valueLength% characters long."),//
                     optionalErrorMsg = @tr("You forgot to write a team name"))
    @Optional
    private final String displayName;

    @RequestParam(role = Role.POST)
    @ParamConstraint(min = "4",
                     minErrorMsg = @tr("The contact size has to be superior to %constraint% but your text is %valueLength% characters long."),//
                     max = "300", maxErrorMsg = @tr("The contact size has to be inferior to %constraint%."),//
                     optionalErrorMsg = @tr("You forgot to write a specification"))
    @Optional
    private final String contact;

    @RequestParam(role = Role.POST)
    @ParamConstraint(
                     min = "4",
                     minErrorMsg = @tr("Number of characters for description has to be superior to %constraint% but your text is %valueLength% characters long."),//
                     max = "5000",
                     maxErrorMsg = @tr("Number of characters for description has to be inferior to %constraint% but your text is %valueLength% characters long."),//
                     optionalErrorMsg = @tr("You forgot to write a description"))
    private final String description;

    @RequestParam(role = Role.POST)
    private final String right;

    private final CreateTeamActionUrl url;

    public CreateTeamAction(final CreateTeamActionUrl url) {
        super(url);
        this.url = url;
        this.contact = url.getContact();
        this.description = url.getDescription();
        this.login = url.getLogin();
        this.right = url.getRight();
        this.displayName = url.getDisplayName();
    }

    @Override
    protected Url doCheckRightsAndEverything(final Member me) {
        return NO_ERROR;
    }

    @Override
    public Url doProcessRestricted(final Member me) {
        Right teamRight = Right.PUBLIC;
        if (right.equals(PUBLIC)) {
            teamRight = Right.PUBLIC;
        } else if (right.equals(PROTECTED)) {
            teamRight = Right.PROTECTED;
        } else {
            session.notifyBad(Context.tr("A team can either be public or protected (and dude, stop playing with our post data)."));
            transmitParameters();
            return new CreateTeamPageUrl();
        }
        final Team newTeam = new Team(login, contact, description, teamRight, me);
        if (displayName != null) {
            try {
                newTeam.setDisplayName(displayName);
            } catch (final UnauthorizedOperationException e) {
                session.notifyBad(Context.tr("You are not authorized to change the display name."));
            }
        }

        return new TeamPageUrl(newTeam);
    }

    @Override
    protected Url doProcessErrors() {
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
        session.addParameter(url.getDisplayNameParameter());
    }
}
