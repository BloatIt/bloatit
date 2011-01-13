/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. BloatIt is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details. You should have received a copy of the GNU Affero General
 * Public License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.actions;

import com.bloatit.common.UnauthorizedOperationException;
import com.bloatit.framework.Kudosable;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.ParamContainer;
import com.bloatit.web.annotations.RequestParam;
import com.bloatit.web.exceptions.RedirectException;
import com.bloatit.web.server.Context;
import com.bloatit.web.utils.url.KudoActionUrl;
import com.bloatit.web.utils.url.Url;

/**
 * A response to a form used to kudo any <code>kudosable</code> on the bloatit website
 */
@ParamContainer("action/kudo")
public final class KudoAction extends LoggedAction {

    public static final String TARGET_KUDOSABLE = "targetKudosable";

    @RequestParam(name = TARGET_KUDOSABLE, level = Level.ERROR)
    private final Kudosable targetKudosable;

    private final KudoActionUrl url;

    public KudoAction(final KudoActionUrl url) {
        super(url);
        this.url = url;
        this.targetKudosable = url.getTargetKudosable();
        session.notifyList(url.getMessages());
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        // Authentication
        targetKudosable.authenticate(session.getAuthToken());

        try {
            targetKudosable.kudos();
            session.notifyGood(Context.tr("Kudo applied"));
        } catch (UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to kudo that."));
        }

        return session.pickPreferredPage();
    }

    @Override
    protected Url doProcessErrors() throws RedirectException {
        session.notifyList(url.getMessages());
        return session.pickPreferredPage();
    }

    @Override
    protected String getRefusalReason() {
        return "You must be logged to kudo";
    }

    @Override
    protected void transmitParameters() {
        // Nothing to save
    }
}
