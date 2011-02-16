package com.bloatit.web.pages.team;

import com.bloatit.data.DaoGroup.Right;
import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Group;
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

    @RequestParam(name = CONTACT_CODE, role = Role.POST, defaultValue = "")
    @ParamConstraint(max = "300", maxErrorMsg = @tr("Number of characters for contact has to be inferior to 300"))
    private final String contact;

    @RequestParam(name = DESCRIPTION_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for description has to be superior to 5"),//
    max = "5000", maxErrorMsg = @tr("Number of characters for description has to be inferior to 5000"))
    private final String description;

    @RequestParam(name = RIGHTS_CODE, role = Role.POST, level = Level.ERROR)
    private final String right;

    private CreateTeamActionUrl url;

    public CreateTeamAction(CreateTeamActionUrl url) {
        super(url);
        this.url = url;
        this.contact = url.getContact();
        this.description = url.getDescription();
        this.login = url.getLogin();
        this.right = url.getRight();
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        Right groupRight = Right.PUBLIC; 
        if (right.equals(PUBLIC)) {
            groupRight = Right.PUBLIC;
        } else if (right.equals(PROTECTED)) {
            groupRight = Right.PROTECTED;
        } else {
            session.notifyBad(Context.tr("A team can either be public or protected (and dude, stop playing with our post data)"));
            transmitParameters();
            throw new RedirectException(new CreateTeamPageUrl());
        }
        Group newGroup = new Group(login, contact, description, groupRight, session.getAuthToken().getMember());

        return new TeamPageUrl(newGroup);
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
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
