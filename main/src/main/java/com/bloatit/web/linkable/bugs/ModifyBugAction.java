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
package com.bloatit.web.linkable.bugs;

import static com.bloatit.framework.webserver.Context.tr;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.exceptions.general.BadProgrammerException;
import com.bloatit.framework.exceptions.general.ShallNotPassException;
import com.bloatit.framework.exceptions.specific.UnauthorizedOperationException;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Optional;
import com.bloatit.framework.webserver.annotations.ParamConstraint;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.annotations.RequestParam.Role;
import com.bloatit.framework.webserver.annotations.tr;
import com.bloatit.framework.webserver.masters.Action;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.model.feature.FeatureManager;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.LoginPageUrl;
import com.bloatit.web.url.ModifyBugActionUrl;

/**
 * A response to a form used to create a new feature
 */
@ParamContainer("feature/bug/domodify")
public final class ModifyBugAction extends Action {

    public static final String BUG_STATE = "bug_state";
    public static final String BUG_REASON = "reason";
    public static final String BUG_LEVEL = "bug_level";
    public static final String BUG = "bug";

    @RequestParam(name = BUG_REASON, role = Role.POST)
    @Optional
    @ParamConstraint(max = "120", maxErrorMsg = @tr("The reason must be 120 chars length max."), //
    min = "0")
    private final String reason;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug level"))
    @RequestParam(name = BUG_LEVEL, role = Role.POST)
    private final BindedLevel level;

    @ParamConstraint(optionalErrorMsg = @tr("You must indicate a bug state"))
    @RequestParam(name = BUG_STATE, role = Role.POST)
    private final BindedState state;

    @ParamConstraint(optionalErrorMsg = @tr("A bug change must be linked to a bug"))
    @RequestParam(name = BUG, role = Role.GET)
    private final Bug bug;

    private final ModifyBugActionUrl url;

    public ModifyBugAction(final ModifyBugActionUrl url) {
        super(url);
        this.url = url;

        this.level = url.getLevel();
        this.state = url.getState();
        this.bug = url.getBug();
        this.reason = url.getReason();

    }

    @Override
    protected Url doProcess() {
        session.notifyList(url.getMessages());
        if (!FeatureManager.canCreate(session.getAuthToken())) {
            // TODO: use BugManager and not FeatureManager here
            session.notifyError(Context.tr("You must be logged in to report a bug."));
            return new LoginPageUrl();
        }

        Level currentLevel = bug.getErrorLevel();
        BugState currentState = bug.getState();

        if (currentLevel == level.getLevel() && currentState == state.getState()) {
            session.notifyBad(Context.tr("You must change at least a small thing on the bug to modify it."));
            return redirectWithError();
        }

        if (state.getState() == BugState.PENDING) {
            session.notifyBad(Context.tr("You can set a bug to the pending state."));
            return redirectWithError();
        }

        String changes = "";

        if (currentLevel != level.getLevel()) {
            changes += tr("\nLevel: {0} => {1}", BindedLevel.getBindedLevel(currentLevel), level);
        }

        if (currentState != state.getState()) {
            changes += tr("\nState: {0} => {1}", BindedState.getBindedState(currentState), state);
        }

        bug.setErrorLevel(level.getLevel());
        if (state.getState() == BugState.DEVELOPING) {
            bug.setDeveloping();
        } else if (state.getState() == BugState.RESOLVED) {
            bug.setResolved();
        }

        try {
            if (reason == null) {
                bug.addComment(changes);
            } else {
                bug.addComment(changes + "\n" + tr("Reason:") + "\n" + reason);
            }
        } catch (UnauthorizedOperationException e) {
            // If the user can change state it should be able to add a comment
            throw new ShallNotPassException("The user can change the bug state but not post comments on this bug");
        }

        final BugPageUrl to = new BugPageUrl(bug);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        session.notifyList(url.getMessages());

        return redirectWithError();
    }

    public Url redirectWithError() {

        session.addParameter(url.getReasonParameter());
        session.addParameter(url.getBugParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getStateParameter());
        return Context.getSession().getLastVisitedPage();
    }

}
