package com.bloatit.web.actions;

import org.apache.commons.lang.NotImplementedException;

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
import com.bloatit.web.url.CreateTeamActionUrl;
import com.bloatit.web.url.CreateTeamPageUrl;
import com.bloatit.web.url.TeamPageUrl;

@ParamContainer("team/docreate")
public class CreateTeamAction extends LoggedAction {
    public final static String PROTECTED = "PROTECTED";
    public final static String PUBLIC = "PUBLIC";

    public static final String LOGIN_CODE = "bloatit_login";
    public static final String EMAIL_CODE = "bloatit_email";
    public static final String RIGHTS_CODE = "bloatit_team_rights";

    @RequestParam(name = LOGIN_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters team name has to be superior to 4"),//
    max = "30", maxErrorMsg = @tr("Number of characters for team has to be inferior to 30"))
    private final String login;

    @RequestParam(name = EMAIL_CODE, role = Role.POST)
    @ParamConstraint(min = "4", minErrorMsg = @tr("Number of characters for email has to be superior to 5"),//
    max = "30", maxErrorMsg = @tr("Number of characters for email address has to be inferior to 30"))
    private final String email;

    @RequestParam(name = RIGHTS_CODE, role = Role.POST, level = Level.ERROR)
    private final String right;

    private CreateTeamActionUrl url;

    public CreateTeamAction(CreateTeamActionUrl url) {
        super(url);
        this.url = url;
        this.email = url.getEmail();
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
            // TODO save parameters
            throw new RedirectException(new CreateTeamPageUrl());
        }
        Group newGroup = new Group(login, email, groupRight, session.getAuthToken().getMember());

        return new TeamPageUrl(newGroup);
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        // TODO
        throw new NotImplementedException();
    }

    @Override
    protected String getRefusalReason() {
        return Context.tr("You must be logged to create a new team");
    }

    @Override
    protected void transmitParameters() {

    }
}
