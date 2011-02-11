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

import java.util.EnumSet;

import com.bloatit.framework.exceptions.RedirectException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.webserver.Context;
import com.bloatit.framework.webserver.annotations.Message.Level;
import com.bloatit.framework.webserver.annotations.ParamContainer;
import com.bloatit.framework.webserver.annotations.RequestParam;
import com.bloatit.framework.webserver.url.Url;
import com.bloatit.model.KudosableInterface;
import com.bloatit.web.url.PopularityVoteActionUrl;

/**
 * A response to a form used to assess any <code>kudosable</code> on the bloatit website
 */
@ParamContainer("popularity/vote")
public final class PopularityVoteAction extends LoggedAction {

    public static final String TARGET_KUDOSABLE = "targetKudosable";
    public static final String VOTE_UP = "voteUp";

    @RequestParam(name = TARGET_KUDOSABLE, level = Level.ERROR)
    private final KudosableInterface<?> targetKudosable;

    @RequestParam(name = VOTE_UP, level = Level.ERROR)
    private final Boolean voteUp;

    private final PopularityVoteActionUrl url;

    public PopularityVoteAction(final PopularityVoteActionUrl url) {
        super(url);
        this.url = url;
        this.targetKudosable = url.getTargetKudosable();
        this.voteUp = url.getVoteUp();
        session.notifyList(url.getMessages());
    }

    @Override
    public Url doProcessRestricted() throws RedirectException {
        // Authentication
        targetKudosable.authenticate(session.getAuthToken());

        try {
            if(voteUp) {
                EnumSet<SpecialCode> canVote = targetKudosable.canVoteUp();

                if(canVote.isEmpty()) {
                    int weight = targetKudosable.voteUp();
                    session.notifyGood(Context.tr("Vote up applied: {0}", weight));
                } else {
                    analyseErrors(canVote);
                }
            } else {
                EnumSet<SpecialCode> canVote = targetKudosable.canVoteDown();

                if(canVote.isEmpty()) {
                    int weight = targetKudosable.voteDown();
                    session.notifyGood(Context.tr("Vote down applied: {0}", weight));
                } else {
                    analyseErrors(canVote);
                }
            }
        } catch (final UnauthorizedOperationException e) {
            session.notifyBad(Context.tr("For obscure reasons, you are not allowed to vote on that."));
        }

        return session.pickPreferredPage();
    }

    public void analyseErrors(EnumSet<SpecialCode> canVote) {
        if(canVote.contains(SpecialCode.ALREADY_VOTED)) {
            session.notifyBad(Context.tr("You already voted on that."));
        }
        if(canVote.contains(SpecialCode.INFLUENCE_LOW_ON_VOTE_UP)) {
            session.notifyBad(Context.tr("You have a too low reputation to vote up that."));
        }
        if(canVote.contains(SpecialCode.INFLUENCE_LOW_ON_VOTE_DOWN)) {
            session.notifyBad(Context.tr("You have a too low reputation to vote down that."));
        }
        if(canVote.contains(SpecialCode.OWNED_BY_ME)) {
            session.notifyBad(Context.tr("You can't vote for yourself!."));
        }
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
