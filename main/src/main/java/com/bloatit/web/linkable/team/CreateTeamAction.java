package com.bloatit.web.linkable.team;

import com.bloatit.data.DaoTeam.Right;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Team;
import com.bloatit.model.Member;
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
public class CreateTeamAction extends LoggedAction {
    public final static String PROTECTED = "PROTECTED";
    public final static String PUBLIC = "PUBLIC";

    public static final String LOGIN_CODE = "bloatit_login";
    public static final String CONTACT_CODE = "bloatit_email";
    public static final String RIGHTS_CODE = "bloatit_team_rights";
    public static final String DESCRIPTION_CODE = "bloatit_team_description";

    @RequestParam(name = LOGIN_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters team name has to be superior to 4"),//
    max = "50", maxErrorMsg = @tr("Number of characters for team name has to be inferior to 50"))
    private final String login;

    @RequestParam(name = CONTACT_CODE, role = Role.POST)
    @ParamConstraint(max = "300", maxErrorMsg = @tr("Number of characters for contact has to be inferior to 300"))
    @Optional
    private final String contact;

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for description has to be superior to 5"),//
    max = "5000", maxErrorMsg = @tr("Number of characters for description has to be inferior to 5000"))
    private final String description;

    @RequestParam(name = RIGHTS_CODE, role = Role.POST)
    private final String right;

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
    public Url doProcessRestricted(Member authenticatedMember) {
        Right teamRight = Right.PUBLIC;
        if (right.equals(PUBLIC)) {
            teamRight = Right.PUBLIC;
        } else if (right.equals(PROTECTED)) {
            teamRight = Right.PROTECTED;
        } else {
            session.notifyBad(Context.tr("A team can either be public or protected (and dude, stop playing with our post data)"));
            transmitParameters();
            return new CreateTeamPageUrl();
        }
        final Team newTeam = new Team(login, contact, description, teamRight, session.getAuthToken().getMember());

        return new TeamPageUrl(newTeam);
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());
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
