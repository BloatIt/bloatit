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

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.RequestParam.Role;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.framework.webprocessor.masters.Action;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Bug;
import com.bloatit.web.url.BugPageUrl;
import com.bloatit.web.url.ModifyBugActionUrl;
import com.bloatit.web.url.ModifyBugPageUrl;

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
        final Level currentLevel = bug.getErrorLevel();
        final BugState currentState = bug.getState();

        if (currentLevel == level.getLevel() && currentState == state.getState()) {
            session.notifyBad(Context.tr("You must change at least a small thing on the bug to modify it."));
            return doProcessErrors();
        }

        if (state.getState() == BugState.PENDING && currentState != BugState.PENDING) {
            session.notifyBad(Context.tr("You cannot set a bug to the pending state."));
            return doProcessErrors();
        }

        String changes = "";
        if (currentLevel != level.getLevel()) {
            changes += tr("%LEVEL% {0} => {1}", "%"+BindedLevel.getBindedLevel(currentLevel)+"%", "%"+level+"%")+"\n";
        }

        if (currentState != state.getState()) {
            changes += tr("%STATE% {0} => {1}", "%"+BindedState.getBindedState(currentState)+"%", "%"+state+"%")+"\n";
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
                bug.addComment(changes + "\n%REASON%\n" + reason);
            }
        } catch (final UnauthorizedOperationException e) {
            session.notifyError(Context.tr("An error prevented us from accessing changing state on this bug. Please notify us."));
            throw new ShallNotPassException("The user can change the bug state but not post comments on this bug");
        }
        
        final BugPageUrl to = new BugPageUrl(bug);

        return to;
    }

    @Override
    protected Url doProcessErrors() {
        return Context.getSession().getLastVisitedPage();
    }

    @Override
    protected Url checkRightsAndEverything() {
        if (session.getAuthToken() == null) {
            session.notifyError(Context.tr("You must be logged in to modify a bug report."));
            return new ModifyBugPageUrl(bug);
        }
        return NO_ERROR;
    }

    @Override
    protected void transmitParameters() {
        session.addParameter(url.getReasonParameter());
        session.addParameter(url.getBugParameter());
        session.addParameter(url.getLevelParameter());
        session.addParameter(url.getStateParameter());
    }

}
